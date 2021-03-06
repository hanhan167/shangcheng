package net.lab1024.smartadmin.module.business.goods;


import net.lab1024.smartadmin.common.constant.ResponseCodeConst;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.DealTypeEnum;
import net.lab1024.smartadmin.constant.StatusEnum;
import net.lab1024.smartadmin.module.business.goods.constant.GoodsResponseCodeConst;
import net.lab1024.smartadmin.module.business.goods.constant.ModelTypeEnum;
import net.lab1024.smartadmin.module.business.goods.dao.GoodsDao;
import net.lab1024.smartadmin.module.business.goods.dao.StyleGoodsDao;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsVO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.PageQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.TypeAndIdDTO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleGoodsEntity;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;
import net.lab1024.smartadmin.module.support.file.service.FileService;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.util.BusinessException;
import net.lab1024.smartadmin.util.SmartBeanUtil;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private StyleGoodsService styleGoodsService;

    @Autowired
    private FileService fileService;

    @Autowired
    private StyleService styleService;

    @Autowired
    private StyleGoodsDao styleGoodsDao;


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
            queryFields.put("sortFields","tg.create_time desc");
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
                queryFields.put("startPrice",Integer.valueOf(s1));
                queryFields.put("endPrice",Integer.valueOf(s2));
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
            List<FileEntity> fileEntityList = fileService.selectFile(ModelTypeEnum.GOODS.getValue(), id);
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
                //检查是否有对应的数据
                if(styleService.selectByKey(styleId)==null){
                    throw new BusinessException(ResponseCodeConst.NOT_EXISTS.getCode(), "找不到对应风格数据!");
                }
                styleGoodsService.save(StyleGoodsEntity.builder().goodsId(goodsEntity.getId()).styleId(styleId).createTime(new Date()).createUserId(Integer.valueOf(requestToken.getRequestUserId().toString())).build());
            }
            //新增文件
            List<FileEntity> fileEntityList = fileService.saveFileList(goodsEntity.getFileList(), goodsEntity.getId(),requestToken.getRequestUserId());
            goodsEntity.setFileList(fileEntityList);
            return ResponseDTO.succData(goodsEntity);
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
            //更新商品表
            newGoodsEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
            newGoodsEntity.setUpdateTime(new Date());
            newGoodsEntity.setDeleted(StatusEnum.NORMAL.getValue());
            goodsDao.updateByKey(newGoodsEntity);
            //保存多对多表
            List<Integer> stypeList = newGoodsEntity.getStypeList();
            for(Integer styleId:stypeList){
                //检查是否有对应的数据
                if(styleService.selectByKey(styleId)==null){
                    throw new BusinessException(ResponseCodeConst.NOT_EXISTS.getCode(), "找不到对应风格数据!");
                }
                styleGoodsService.save(StyleGoodsEntity.builder().goodsId(goodsEntity.getId()).styleId(styleId).createTime(new Date()).createUserId(Integer.valueOf(requestToken.getRequestUserId().toString())).build());
            }
            //新增文件表
            List<FileEntity> fileEntityList = fileService.saveFileList(fileList, newGoodsEntity.getId(), requestToken.getRequestUserId());
            newGoodsEntity.setFileList(fileEntityList);
            return ResponseDTO.succData(newGoodsEntity);
        }
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

    /**
     * 查询是否有对应的商品数据
     * @param brandId
     * @param styleId
     * @return
     */
    public boolean checkIsNotLiveGoods(Integer brandId, Integer styleId) {
        List<GoodsEntity> goodsEntityList = null;
        if(brandId!=null){
            goodsEntityList = goodsDao.selectList(GoodsEntity.
                    builder().
                    brandId(brandId).
                    deleted(StatusEnum.NORMAL.getValue()).
                    build());
        }else{
            List<StyleGoodsEntity> styleGoodsEntityList = styleGoodsDao.selectList(StyleGoodsEntity.
                    builder().
                    styleId(styleId).
                    build());
            if(styleGoodsEntityList!=null && styleGoodsEntityList.size()>0){
                List<Integer> goodsIdList = styleGoodsEntityList.stream().map(val -> val.getGoodsId()).collect(Collectors.toList());
                //查询是否有对应的正常的商品信息
                goodsEntityList =  goodsDao.selectNormalOrNotGoods(goodsIdList,StatusEnum.NORMAL.getValue());
            }
        }
        if(goodsEntityList!=null && goodsEntityList.size()>0) {
            return false;
        }
        return true;
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
            List<FileEntity> styleFileEntityList = fileService.selectFile(ModelTypeEnum.STYLE.getValue(), val.getId());
            val.setFileEntityList(styleFileEntityList);
        });
        goodsVO.setStyleEntityList(styleEntityList);
        //品牌
        List<FileEntity> brandFileEntityList = fileService.selectFile(ModelTypeEnum.BRAND.getValue(), goodsVO.getBrandId());
        goodsVO.setBrandFileList(brandFileEntityList);
        //商品
        List<FileEntity> goodsFileEntityList = fileService.selectFile(ModelTypeEnum.GOODS.getValue(), goodsVO.getId());
        goodsVO.setGoodsFileList(goodsFileEntityList);

        return ResponseDTO.succData(goodsVO);
    }
}
