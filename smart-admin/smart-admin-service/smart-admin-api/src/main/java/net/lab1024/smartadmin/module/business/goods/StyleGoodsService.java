package net.lab1024.smartadmin.module.business.goods;

import net.lab1024.smartadmin.module.business.goods.dao.StyleGoodsDao;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleGoodsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StyleGoodsService {

    @Autowired
    private StyleGoodsDao styleGoodsDao;


    @Transactional(rollbackFor = Exception.class)
    public StyleGoodsEntity save(StyleGoodsEntity styleGoodsEntity){
        styleGoodsDao.save(styleGoodsEntity);
        return styleGoodsEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<StyleGoodsEntity> selectList(StyleGoodsEntity styleGoodsEntity){
        return styleGoodsDao.selectList(styleGoodsEntity);
    }





    public void updateByKey(StyleGoodsEntity val) {
        styleGoodsDao.updateByKey(val);
    }

    public int deleteByGoodsId(Integer id) {
        return styleGoodsDao.deleteByGoodsId(id);
    }
}
