package net.lab1024.smartadmin.common.base;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenkai_yy
 * @dateTime 2018/9/29 17:29
 * @className com.migu.base.BaseDao
 * @description
 **/
public interface BaseDao<T, KEY extends Serializable>{
    /**
     * 通用的保存方法
     * @param entity
     */
    void save(T entity);

    /**
     * 批量保存
     * @param list
     */
    void batchSave(List<T> list);

    /**
     * 通用的修改方法
     * @param entity
     */
    int updateByKey(T entity);

    /**
     * 根据条件删除方法
     * @param entity
     */
    int deleteByCondition(T entity);

    /**
     * 通过ID查询
     * @param key
     * @return
     */
    T selectByKey(KEY key);

    /**
     * 查询记录集合
     * @param entity
     * @return
     */
    List<T> selectList(T entity);

    /**
     * 查询总条数
     * @param entity
     * @return
     */
    Integer selectListCount(T entity);
}
