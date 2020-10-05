package net.lab1024.smartadmin.module.business.goods.dao;


import net.lab1024.smartadmin.common.base.BaseDao;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import org.apache.ibatis.annotations.Param;
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
public interface GoodsDao extends BaseDao<GoodsEntity, Integer> {


    List<GoodsEntity> selectByTypeId(@Param("id") Integer id);
}
