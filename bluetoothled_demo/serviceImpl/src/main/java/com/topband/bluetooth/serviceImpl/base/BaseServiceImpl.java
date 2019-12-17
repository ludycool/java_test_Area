package com.topband.bluetooth.serviceImpl.base;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.topband.bluetooth.common.model.SystemConstants;
import com.topband.bluetooth.common.util.JsonHelper;
import com.topband.bluetooth.common.util.StringHelper;
import com.topband.bluetooth.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ludi
 * @version 1.0
 * @date 2019/10/21 13:45
 * @remark 默认库 读写分离通用模板
 * 如需访问自定库：可直接继承MyServiceImpl 并在类上添加DS("node")注解
 * 如果在maper 层使用了DS注解，那在应用层 再使用DS 不会再生效，如果maper ,server等底层没有使用DS，应用层使用可以生效
 * <p>
 * <p>
 * DS注解可以切换数据节点
 * 1、没有@DS	默认主库master
 * 2、 @DS("slave")、@DS("slave_1"),指定组，或者指定一个节点，不存在则使用主库
 * 3、@DS 根据DynamicDataSourceStrategy策略，选择一个从库。默认负载均衡策略。
 */

public class BaseServiceImpl<M extends BaseMapper<T>, T> extends MyServiceImpl<M, T> {


    // region -------------------------- mybatis plus 通用方法 重写 使用从库读操作---------------------------------------------

    @DS("slave")
    @Override
    public T getById(Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @DS("slave")
    @Override
    public Collection<T> listByIds(Collection<? extends Serializable> idList) {
        return baseMapper.selectBatchIds(idList);
    }

    @DS("slave")
    @Override
    public Collection<T> listByMap(Map<String, Object> columnMap) {
        return baseMapper.selectByMap(columnMap);
    }

    @DS("slave")
    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        if (throwEx) {
            return baseMapper.selectOne(queryWrapper);
        }
        return SqlHelper.getObject(log, baseMapper.selectList(queryWrapper));
    }

    @DS("slave")
    @Override
    public Map<String, Object> getMap(Wrapper<T> queryWrapper) {
        return SqlHelper.getObject(log, baseMapper.selectMaps(queryWrapper));
    }

    @DS("slave")
    @Override
    public int count(Wrapper<T> queryWrapper) {
        return SqlHelper.retCount(baseMapper.selectCount(queryWrapper));
    }

    @DS("slave")
    @Override
    public List<T> list(Wrapper<T> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @DS("slave")
    @Override
    public IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper) {
        return baseMapper.selectPage(page, queryWrapper);
    }

    @DS("slave")
    @Override
    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return baseMapper.selectMaps(queryWrapper);
    }

    @DS("slave")
    @Override
    public <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return baseMapper.selectObjs(queryWrapper).stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    @DS("slave")
    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper) {
        return baseMapper.selectMapsPage(page, queryWrapper);
    }

    @DS("slave")
    @Override
    public <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return SqlHelper.getObject(log, listObjs(queryWrapper, mapper));
    }

    //endregion

    //region  ---------------------------------------自定义方法 通用方法-------------------------------------------------------

    @Autowired
    protected CommonMapper commonMapper;

    public CommonMapper getCommonMapper() {
        return commonMapper;
    }

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 根据条件部分更新
     *
     * @param item    实体对象
     * @param columns 需要 更新的列
     * @param wrapper 条件 条件不传， 根据主键更新
     * @param <T>
     * @return
     */
    public <T> int updataInByWrapper(T item, List<String> columns, Wrapper wrapper) {
        Class<?> cls = item.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        String tableName = tableInfo.getTableName();

        Map<String, Object> map = JsonHelper.object2Map(item);
        if (wrapper == null)// 条件不传， 根据主键更新
        {
            wrapper = new QueryWrapper<>();
            String primaryKey = tableInfo.getKeyProperty();
            ((QueryWrapper) wrapper).eq(primaryKey, map.get(primaryKey));
        }
        String[] keys = new String[map.keySet().size()];
        map.keySet().toArray(keys);
        for (String key : keys) {
            if (!columns.contains(key)) {
                map.remove(key);
            }
            /*
            else {//时间类型过滤，mysql 不用，pgsql需要时间格式
                Object v = map.get(key);
                if (StringHelper.isDate(String.valueOf(v))) {
                    try {
                        map.put(key, formatter.parse(v.toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
               */
            /*else if(key.contains("time")){//时间类型过滤，mysql 不用，pgsql需要时间格式
                Object v=map.get(key);
                    try {
                        map.put(key,formatter.parse(v.toString()));
                    } catch (ParseException e) {
                        //e.printStackTrace();
                    }
            }*/
        }
        if (SystemConstants.DB_FIELD_CAMEL_CASE)//数据库转实体 字段是否转下划线驼峰且首字母小写
        {
            map = mapFieldCamelCase(map);
        }
        return (int) commonMapper.update(tableName, map, wrapper);
    }


    /**
     * 根据条件部分更新 字段为空，不更新
     *
     * @param item    实体对象
     * @param columns 不需要 更新的列
     * @param wrapper 条件  条件不传， 根据主键更新
     * @param <T>
     * @return
     */
    public <T> int updataNotInByWrapper(T item, List<String> columns, Wrapper wrapper) {
        Class<?> cls = item.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        String tableName = tableInfo.getTableName();
        String primaryKey = tableInfo.getKeyProperty();//主键不更新

        Map<String, Object> map = JsonHelper.object2Map(item);
        if (wrapper == null)// 条件不传， 根据主键更新
        {
            wrapper = new QueryWrapper<>();
            ((QueryWrapper) wrapper).eq(primaryKey, map.get(primaryKey));
        }
        map.remove(primaryKey);//主键不更新
        String[] keys = new String[map.keySet().size()];
        map.keySet().toArray(keys);
        for (String key : keys) {
            if (columns.contains(key) || map.get(key) == null) {
                map.remove(key);
            }
        }
        if (SystemConstants.DB_FIELD_CAMEL_CASE)//数据库转实体 字段是否转下划线驼峰且首字母小写
        {
            map = mapFieldCamelCase(map);
        }
        return (int) commonMapper.update(tableName, map, wrapper);
    }

    /**
     * map 中字段驼峰转下划线 用于数据库更新字段
     *
     * @param map
     * @return
     */
    Map<String, Object> mapFieldCamelCase(Map<String, Object> map) {
        Map<String, Object> map2 = new HashMap<>();
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            map2.put(StringHelper.underscoreName(entry.getKey()), entry.getValue());
        }
        return map2;
    }

    //endregion


    //region------------------------------- 实现 自定义通用接口  -------------------------------------------------------

    /**
     * 根据条件部分更新
     *
     * @param tableName  表名称
     * @param primaryKey 主键名称
     * @param map        id及包括需要更新的字段 键值
     * @return
     */
    public boolean updateInById(String tableName, String primaryKey, Map<String, Object> map) {
        Wrapper wrapper = new QueryWrapper<>().eq(primaryKey, map.get(primaryKey));
        if (map.containsKey(primaryKey)) {
            map.remove(primaryKey);//主键不更新
        }
        if (SystemConstants.DB_FIELD_CAMEL_CASE)//数据库转实体 字段是否转下划线驼峰且首字母小写
        {
            map = mapFieldCamelCase(map);
        }
        return SqlHelper.retBool((int) commonMapper.update(tableName, map, wrapper));
    }

    /**
     * 根据主键 部分更新
     *
     * @param item    实体对象
     * @param columns 需要 更新的列
     * @param <T>
     * @return
     */
    public <T> boolean updateInById(T item, List<String> columns) {
        return SqlHelper.retBool(updataInByWrapper(item, columns, null));
    }

    /**
     * 根据主键 部分更新
     *
     * @param item    实体对象
     * @param columns 不需要 更新的列
     * @param <T>
     * @return
     */
    public <T> boolean updateNotInById(T item, List<String> columns) {
        return SqlHelper.retBool(updataNotInByWrapper(item, columns, null));
    }

    /**
     * 条件查询
     *
     * @param field      所需要的字段
     * @param tableName  表名
     * @param whereOject And条件  格式参考  QueryUtil 里说明 {@link QueryUtil}
     * @return
     */
    @DS("slave")
    public List<LinkedHashMap<String, Object>> listMapByWhere(String field, String tableName, JSONObject whereOject) {
        QueryWrapper wrapper = QueryUtil.getQuery(whereOject);
        return commonMapper.listMapByWrapper(field, tableName, wrapper);
    }

    /**
     * 条件查询 分页
     *
     * @param field      所需要的字段
     * @param tableName  表名
     * @param whereOject And条件  格式参考 QueryUtil 里说明 {@link QueryUtil}
     * @return
     */
    @DS("slave")
    public IPage<LinkedHashMap<String, Object>> listMapByWhere(IPage page, String field, String tableName, JSONObject whereOject) {
        QueryWrapper wrapper = QueryUtil.getQuery(whereOject);
        return commonMapper.listMapByWrapper(page, field, tableName, wrapper);
    }

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    @DS("slave")
    public List<LinkedHashMap<String, Object>> listMapByMap(String field, String tableName, Map<String, Object> columnMap) {
        QueryWrapper wrapper = QueryUtil.getQueryByColumnMap(columnMap);
        if (wrapper == null) {
            return new ArrayList<>();
        }
        return commonMapper.listMapByWrapper(field, tableName, wrapper);
    }

    /**
     * 查询（根据 columnMap 条件） 分页
     *
     * @param columnMap 表字段 map 对象 可包含排序 条件，会自动排除，字段参考 QueryUtil 里说明 {@link QueryUtil}
     */
    @DS("slave")
    public IPage<LinkedHashMap<String, Object>> listMapByMap(IPage page, String field, String tableName, Map<String, Object> columnMap) {
        QueryUtil.removePageKey(columnMap);
        QueryWrapper wrapper = QueryUtil.getQueryByColumnMap(columnMap);
        if (wrapper == null) {
            return null;
        }
        return commonMapper.listMapByWrapper(page, field, tableName, wrapper);
    }
}
