package net.lab1024.smartadmin.module.business.goods.dao;


import net.lab1024.smartadmin.common.base.BaseDao;
import net.lab1024.smartadmin.module.business.goods.domain.entity.BrandEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019-07-11 16:19:48
 * @since JDK1.8
 */
@Repository
public interface StyleDao extends BaseDao<StyleEntity, Integer> {


    List<StyleEntity> selectStyleList(StyleEntity styleEntity);
}
