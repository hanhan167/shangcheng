package net.lab1024.smartadmin.module.business.goods;

import net.lab1024.smartadmin.common.constant.JudgeEnum;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.StatusEnum;
import net.lab1024.smartadmin.module.business.goods.constant.GoodsResponseCodeConst;
import net.lab1024.smartadmin.module.business.goods.dao.GoodsDao;
import net.lab1024.smartadmin.module.business.goods.dao.StyleGoodsDao;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsVO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.PageQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.TypeAndIdDTO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleGoodsEntity;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private StyleGoodsService styleGoodsService;

    public ResponseDTO<PageResultDTO<GoodsEntity>> queryByPage(PageQueryDTO pageQueryDTO) {
        pageQueryDTO.putPage();
        Map<String, Object> queryFields = pageQueryDTO.getQueryFields();
        boolean sortFields = queryFields.containsKey("sortFields");
        if(!sortFields){
            queryFields.put("sortFields","create_time desc");
        }
        //记录条数
        List<GoodsEntity> goodsEntityList =  goodsDao.selectGoodsListPage(pageQueryDTO);
        Long count = goodsDao.selectGoodsListCount(pageQueryDTO);
        PageResultDTO<GoodsEntity> pageResultDTO = new PageResultDTO();
        pageResultDTO.setList(goodsEntityList);
        pageResultDTO.setTotal(count);
        pageResultDTO.setPageNum(Long.valueOf(pageQueryDTO.getCurrentPage()));
        pageResultDTO.setPageSize(Long.valueOf(pageQueryDTO.getPageSize()));
        return ResponseDTO.succData(pageResultDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<GoodsEntity> saveGoods(GoodsEntity goodsEntity, RequestTokenBO requestToken) {
        goodsEntity.setCreateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        goodsEntity.setCreateTime(new Date());
        goodsEntity.setDeleted(StatusEnum.NORMAL.getValue());
        goodsDao.save(goodsEntity);
        //保存多对多表
        styleGoodsService.save(StyleGoodsEntity.builder().goodsId(goodsEntity.getId()).styleId(goodsEntity.getStypeId()).deleted(StatusEnum.NORMAL.getValue()).build());
        return ResponseDTO.succData(goodsEntity);
    }

    public ResponseDTO<List<GoodsEntity>> findIsNotGoods(TypeAndIdDTO typeAndIdDTO, RequestTokenBO requestToken) {
        //判断类型
        Integer type = typeAndIdDTO.getType();
        Integer id = typeAndIdDTO.getId();
        List<GoodsEntity> goodsEntities = Lists.newArrayList();
        //1:品牌
        if(type==1){
            //查询
            goodsEntities = goodsDao.selectList(GoodsEntity.builder().brandId(id).deleted(JudgeEnum.YES.getValue()).build());
        }else if(type==2){
            goodsEntities = goodsDao.selectByTypeId(id);
        }
        return ResponseDTO.succData(goodsEntities);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<GoodsEntity> updateGoods(GoodsEntity goodsEntity, RequestTokenBO requestToken) {
        goodsEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        goodsEntity.setUpdateTime(new Date());
        goodsEntity.setDeleted(StatusEnum.NORMAL.getValue());
        goodsDao.updateByKey(goodsEntity);
        List<StyleGoodsEntity> styleGoodsEntityList = styleGoodsService.selectList(StyleGoodsEntity.builder().styleId(goodsEntity.getStypeId()).goodsId(goodsEntity.getId()).deleted(StatusEnum.NORMAL.getValue()).build());
        if(!CollectionUtils.isEmpty(styleGoodsEntityList)){
            styleGoodsEntityList.forEach(val->{
                val.setUpdateTime(new Date());
                val.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
                styleGoodsService.updateByKey(val);
            });
        }else{
            styleGoodsService.save(StyleGoodsEntity.builder().goodsId(goodsEntity.getId()).styleId(goodsEntity.getStypeId()).deleted(StatusEnum.NORMAL.getValue()).build());
        }
        return ResponseDTO.succData(goodsEntity);
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
}
