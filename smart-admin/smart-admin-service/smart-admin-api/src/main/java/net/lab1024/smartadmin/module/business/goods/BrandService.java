package net.lab1024.smartadmin.module.business.goods;

import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.business.goods.dao.BrandDao;
import net.lab1024.smartadmin.module.business.goods.dao.GoodsDao;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsVO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.BrandEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    public ResponseDTO<PageResultDTO<GoodsVO>> queryByPage(GoodsQueryDTO queryDTO) {
            return  null;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<BrandEntity> saveBrand(BrandEntity brandEntity, RequestTokenBO requestToken) {
        brandEntity.setCreateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        brandEntity.setCreateTime(new Date());
        brandDao.save(brandEntity);
        return ResponseDTO.succData(brandEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<BrandEntity> updateBrand(BrandEntity brandEntity, RequestTokenBO requestToken) {
        brandEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        brandEntity.setUpdateTime(new Date());
        brandDao.updateByKey(brandEntity);
        return ResponseDTO.succData(brandEntity);
    }
}
