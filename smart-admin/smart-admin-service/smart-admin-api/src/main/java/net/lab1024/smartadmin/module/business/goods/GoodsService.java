package net.lab1024.smartadmin.module.business.goods;

import com.baomidou.mybatisplus.extension.api.R;
import net.lab1024.smartadmin.common.constant.JudgeEnum;
import net.lab1024.smartadmin.common.constant.ResponseCodeConst;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.DealTypeEnum;
import net.lab1024.smartadmin.constant.StatusEnum;
import net.lab1024.smartadmin.module.business.goods.constant.GoodsResponseCodeConst;
import net.lab1024.smartadmin.module.business.goods.constant.ModelTypeEnum;
import net.lab1024.smartadmin.module.business.goods.dao.GoodsDao;
import net.lab1024.smartadmin.module.business.goods.dao.StyleGoodsDao;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsVO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.PageQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.TypeAndIdDTO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleGoodsEntity;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;
import net.lab1024.smartadmin.module.support.file.service.FileService;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.util.SmartBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private StyleGoodsService styleGoodsService;

    @Autowired
    private FileService fileService;

    public static boolean checkPrice(String price){
        String regex = "(^[1-9]([0-9]+)?(\\.[0-9]{1,})?$)|(^[1-9]{1,}$)|(^[0-9]{1}$)|(^[0-9]\\.[0-9]{1,}?$)";
        Pattern pattern = Pattern.compile(regex); //将给定的正则表达式编译到模式中。
        Matcher isNum = pattern.matcher(price);//创建匹配给定输入与此模式的匹配器。
        boolean matches = isNum.matches();//如果匹配成功，则可以通过 start、end 和 group 方法获取更多信息.
        return matches;
    }

    public ResponseDTO<PageResultDTO<GoodsEntity>> queryByPage(PageQueryDTO pageQueryDTO) {
        pageQueryDTO.putPage();
        Map<String, Object> queryFields = pageQueryDTO.getQueryFields();
        boolean sortFields = queryFields.containsKey("sortFields");
        if(!sortFields){
            queryFields.put("sortFields","create_time desc");
        }
        if(queryFields.containsKey("priceRange") && queryFields.get("priceRange")!=null){
            String priceRange = queryFields.get("priceRange").toString();
            queryFields.remove("priceRange");
            int index = priceRange.indexOf("-");
            if(index!=-1){
                String s1 = priceRange.substring(0,index);
                String s2 = priceRange.substring(index+1);
                if(!checkPrice(s1) || !checkPrice(s2)) {
                    return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM_ANY,"传递参数异常！");
                }
                queryFields.put("startPrice",s1);
                queryFields.put("endPrice",s2);
            }else{
                if(!checkPrice(priceRange)) {
                    return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM_ANY,"传递参数异常！");
                }
                queryFields.put("price",priceRange);
            }
        }
        //记录条数
        List<GoodsEntity> goodsEntityList =  goodsDao.selectGoodsListPage(queryFields);
        Long count = goodsDao.selectGoodsListCount(queryFields);
        PageResultDTO<GoodsEntity> pageResultDTO = new PageResultDTO();
        goodsEntityList.forEach(val->{
            Integer id = val.getId();
            List<FileEntity> fileEntityList = fileService.selectFile(ModelTypeEnum.GOODS.getName(), id);
            val.setFileList(fileEntityList);
        });
        pageResultDTO.setList(goodsEntityList);
        pageResultDTO.setTotal(count);
        pageResultDTO.setPageNum(Long.valueOf(pageQueryDTO.getPageNo()));
        pageResultDTO.setPageSize(Long.valueOf(pageQueryDTO.getPageSize()));
        return ResponseDTO.succData(pageResultDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<GoodsEntity> saveOrUpdateGoods(GoodsEntity goodsEntity, RequestTokenBO requestToken,String dealType) {
        if(dealType.equals(DealTypeEnum.ADD.getValue())){
            //新增
            goodsEntity.setCreateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
            goodsEntity.setCreateTime(new Date());
            goodsEntity.setDeleted(StatusEnum.NORMAL.getValue());
            goodsSave(goodsEntity);
            //保存多对多表
            List<Integer> stypeList = goodsEntity.getStypeList();
            for(Integer styleId:stypeList){
                styleGoodsService.save(StyleGoodsEntity.builder().goodsId(goodsEntity.getId()).styleId(styleId).createTime(new Date()).createUserId(Integer.valueOf(requestToken.getRequestUserId().toString())).build());
            }
            //新增文件
            fileService.saveFileList(goodsEntity.getFileList(),goodsEntity.getId());
        }else {
            GoodsEntity newGoodsEntity = SmartBeanUtil.copy(goodsEntity,GoodsEntity.class);
            List<FileEntity> fileList = newGoodsEntity.getFileList();
            Integer id = newGoodsEntity.getId();
            GoodsEntity goodsVal = goodsDao.selectByKey(id);
            if(goodsVal==null){
                return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS,"对应数据不存在！");
            }
            //删除之前的数据
            //多对多表
            styleGoodsService.deleteByGoodsId(id);
            //文件表
            fileService.deleteFilesByModuleId(id.toString());
            //商品表
            deleteGoodsById(id);

            //新增操作
            newGoodsEntity.setId(null);
            newGoodsEntity.setCreateUserId(goodsVal.getCreateUserId());
            newGoodsEntity.setCreateTime(goodsVal.getCreateTime());
            newGoodsEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
            newGoodsEntity.setUpdateTime(new Date());
            newGoodsEntity.setDeleted(StatusEnum.NORMAL.getValue());
            goodsSave(newGoodsEntity);
            //保存多对多表
            List<Integer> stypeList = newGoodsEntity.getStypeList();
            for(Integer styleId:stypeList){
                styleGoodsService.save(StyleGoodsEntity.builder().goodsId(goodsEntity.getId()).styleId(styleId).createTime(new Date()).createUserId(Integer.valueOf(requestToken.getRequestUserId().toString())).build());
            }
            //新增文件表
            fileService.saveFileList(fileList,newGoodsEntity.getId());
        }
        return ResponseDTO.succData(goodsEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteGoodsById(Integer id) {
        return goodsDao.deleteGoodsById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void goodsSave(GoodsEntity goodsEntity){
        goodsDao.save(goodsEntity);
    }


    public ResponseDTO<List<GoodsEntity>> findIsNotGoods(TypeAndIdDTO typeAndIdDTO, RequestTokenBO requestToken) {
        //判断类型
        Integer type = typeAndIdDTO.getType();
        Integer id = typeAndIdDTO.getId();
        List<GoodsEntity> goodsEntities = Lists.newArrayList();
        //1:品牌
        if(type==1){
            //查询
            goodsEntities = goodsDao.selectList(GoodsEntity.builder().brandId(id).deleted(StatusEnum.NORMAL.getValue()).build());
        }else if(type==2){
            goodsEntities = goodsDao.selectByTypeId(id);
        }
        return ResponseDTO.succData(goodsEntities);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<GoodsEntity> delGoods(Integer id, RequestTokenBO requestToken) {
        GoodsEntity goodsEntity = goodsDao.selectByKey(id);
        if(goodsEntity==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.NOT_EXISTS);
        }
        goodsEntity.setDeleted(StatusEnum.DELETED.getValue());
        goodsEntity.setUpdateTime(new Date());
        goodsEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        goodsDao.updateByKey(goodsEntity);
        return ResponseDTO.succ();
    }

    public ResponseDTO<GoodsVO> detailGoods(Integer id, RequestTokenBO requestToken) {
        GoodsVO goodsVO = goodsDao.detailGoods(id);
        if(goodsVO==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.NOT_EXISTS);
        }
        //类型
        List<StyleEntity> styleEntityList = styleGoodsService.selectStyleNameByGoodsId(goodsVO.getId());
        styleEntityList.forEach(val->{
            List<FileEntity> styleFileEntityList = fileService.selectFile(ModelTypeEnum.STYLE.getName(), val.getId());
            val.setFileEntityList(styleFileEntityList);
        });
        goodsVO.setStyleEntityList(styleEntityList);
        //品牌
        List<FileEntity> brandFileEntityList = fileService.selectFile(ModelTypeEnum.BRAND.getName(), goodsVO.getBrandId());
        goodsVO.setBrandFileList(brandFileEntityList);
        //商品
        List<FileEntity> goodsFileEntityList = fileService.selectFile(ModelTypeEnum.GOODS.getName(), goodsVO.getId());
        goodsVO.setGoodsFileList(goodsFileEntityList);

        return ResponseDTO.succData(goodsVO);
    }
}
