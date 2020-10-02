package net.lab1024.smartadmin.module.business.goods;

import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.business.goods.dao.GoodsDao;
import net.lab1024.smartadmin.module.business.goods.dao.StyleDao;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsVO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.BrandEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleEntity;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class StyleService {

    @Autowired
    private StyleDao styleDao;

    public ResponseDTO<PageResultDTO<GoodsVO>> queryByPage(GoodsQueryDTO queryDTO) {
            return  null;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<StyleEntity> saveStyle(StyleEntity styleEntity, RequestTokenBO requestToken) {
        styleEntity.setCreateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        styleEntity.setCreateTime(new Date());
        styleDao.save(styleEntity);
        return ResponseDTO.succData(styleEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<StyleEntity> updateStyle(StyleEntity styleEntity, RequestTokenBO requestToken) {
        styleEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        styleEntity.setUpdateTime(new Date());
        styleDao.updateByKey(styleEntity);
        return ResponseDTO.succData(styleEntity);
    }
}
