package com.topband.bluetooth.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ludi
 * @version 1.0
 * @date 2019/10/30 10:27
 * @remark
 */
public interface BaseService<T> {

    //region ------------------------------------- mybatis plus 原接口 去除queryWrapper条件 部份  ------------------------------------------------------

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    boolean save(T entity);

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveBatch(Collection<T> entityList) {
        return saveBatch(entityList, 1000);
    }

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     * @param batchSize  插入批次数量
     */
    boolean saveBatch(Collection<T> entityList, int batchSize);

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveOrUpdateBatch(Collection<T> entityList) {
        return saveOrUpdateBatch(entityList, 1000);
    }

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     * @param batchSize  每次的数量
     */
    boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize);

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    boolean removeById(Serializable id);

    /**
     * 根据 columnMap 条件，删除记录
     *
     * @param columnMap 表字段 map 对象
     */
    boolean removeByMap(Map<String, Object> columnMap);


    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表
     */
    boolean removeByIds(Collection<? extends Serializable> idList);

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    boolean updateById(T entity);


    /**
     * 根据ID 批量更新
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean updateBatchById(Collection<T> entityList) {
        return updateBatchById(entityList, 1000);
    }

    /**
     * 根据ID 批量更新
     *
     * @param entityList 实体对象集合
     * @param batchSize  更新批次数量
     */
    boolean updateBatchById(Collection<T> entityList, int batchSize);

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     */
    boolean saveOrUpdate(T entity);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    T getById(Serializable id);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表
     */
    Collection<T> listByIds(Collection<? extends Serializable> idList);

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    Collection<T> listByMap(Map<String, Object> columnMap);

    /**
     * 获取对应 entity 的 BaseMapper
     *
     * @return BaseMapper
     */
    BaseMapper<T> getBaseMapper();

    //endregion

    //region   -------------------------------------  自定义通用接口 --------------------------------------------------

    /**
     * 根据主键 部分更新
     *
     * @param item    实体对象
     * @param columns 需要 更新的列
     * @param <T>
     * @return
     */
    <T> boolean updateInById(T item, List<String> columns);

    /**
     * 根据条件部分更新
     *
     * @param tableName  表名称
     * @param primaryKey 主键名称
     * @param map        id及包括需要更新的字段 键值
     * @return
     */
    boolean updateInById(String tableName, String primaryKey, Map<String, Object> map);


    /**
     * 根据主键 部分更新 字段为空，不更新
     *
     * @param item    实体对象
     * @param columns 不需要 更新的列
     * @param <T>
     * @return
     */
    <T> boolean updateNotInById(T item, List<String> columns);


    /**
     * 条件查询
     *
     * @param field      所需要的字段
     * @param tableName  表名
     * @param whereOject And条件  格式参考  QueryUtil 里说明
     * @return
     */
    List<LinkedHashMap<String, Object>> listMapByWhere(String field, String tableName, JSONObject whereOject);


    /**
     * 条件查询 分页
     *
     * @param field      所需要的字段
     * @param tableName  表名
     * @param whereOject And条件  格式参考 QueryUtil 里说明
     * @return
     */
    IPage<LinkedHashMap<String, Object>> listMapByWhere(IPage page, String field, String tableName, JSONObject whereOject);


    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象 可包含排序 条件，会自动排除，字段参考 QueryUtil 里说明 {QueryUtil}
     */
    List<LinkedHashMap<String, Object>> listMapByMap(String field, String tableName,Map<String, Object> columnMap);


    /**
     * 查询（根据 columnMap 条件） 分页
     *
     * @param columnMap 表字段 map 对象 可包含排序 条件，会自动排除，字段参考 QueryUtil 里说明 {QueryUtil}
     */
     IPage<LinkedHashMap<String, Object>> listMapByMap(IPage page, String field, String tableName, Map<String, Object> columnMap);

    //endregion

}
