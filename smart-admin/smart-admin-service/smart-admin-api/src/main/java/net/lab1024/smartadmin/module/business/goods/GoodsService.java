package net.lab1024.smartadmin.module.business.goods;

import net.lab1024.smartadmin.common.constant.JudgeEnum;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.business.goods.constant.GoodsResponseCodeConst;
import net.lab1024.smartadmin.module.business.goods.dao.GoodsDao;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsVO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.TypeAndIdDTO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleEntity;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
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


@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public ResponseDTO<PageResultDTO<GoodsVO>> queryByPage(GoodsQueryDTO queryDTO) {
            return  null;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<GoodsEntity> saveGoods(GoodsEntity goodsEntity, RequestTokenBO requestToken) {
        goodsEntity.setCreateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        goodsEntity.setCreateTime(new Date());
        goodsDao.save(goodsEntity);
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
            goodsEntities = goodsDao.selectList(GoodsEntity.builder().brandId(id).build());
        }else if(type==2){
            goodsEntities = goodsDao.selectByTypeId(id);
        }
        return ResponseDTO.succData(goodsEntities);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<GoodsEntity> updateGoods(GoodsEntity goodsEntity, RequestTokenBO requestToken) {
        goodsEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        goodsEntity.setUpdateTime(new Date());
        goodsDao.updateByKey(goodsEntity);
        return ResponseDTO.succData(goodsEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<GoodsEntity> delGoods(Integer id, RequestTokenBO requestToken) {
        GoodsEntity goodsEntity = goodsDao.selectByKey(id);
        if(goodsEntity==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.NOT_EXISTS);
        }
        goodsEntity.setDeleted(JudgeEnum.NO.getValue());
        goodsEntity.setUpdateTime(new Date());
        goodsEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        goodsDao.updateByKey(goodsEntity);
        return ResponseDTO.succ();
    }
}
