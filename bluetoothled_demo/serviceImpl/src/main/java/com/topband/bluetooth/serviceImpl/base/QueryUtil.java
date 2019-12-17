package com.topband.bluetooth.serviceImpl.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.topband.bluetooth.common.model.SystemConstants;
import com.topband.bluetooth.common.util.ComUtil;
import com.topband.bluetooth.common.util.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
/**
 * 描述:
 * mybatis 条件 帮类
 *
 * @author ludycool
 * @create 2018-10-28 16:44
 */
@Component
public class QueryUtil {
    public static final String PAGE_INDEX_NAME = "page";
    public static final String PAGE_LIMIT_NAME = "limit";
    public static final String PAGE_ORDER_BY_FIELD = "orderByField";
    public static final String PAGE_IS_ASC = "isAsc";
    public static final String PAGE_WHERE = "where";
    public static final String LOGIC_DELETE_KEY = "del_flag";


    @Autowired
    private Environment env;
    //是否逻辑删除
    public static boolean logicDelete;

    @PostConstruct
    public void readConfig() {

        logicDelete = Boolean.valueOf(env.getProperty("config.mybatis-plus.logic-delete"));
    }

    /**
        params={
        "page":1,
        "limit":5,
        "orderByField":"createtime",
        "isAsc":"false",
        "where":[
            {
                "name":"truename|loginname",
                "op":"like",
                "value":"小风"
            },
            {
                "name":"departmentid",
                "op":"eq",
                "value":"1"
            }
        ]
    }
    page 页码 1开始
    limit 每页数据量
    orderByField 排序字段
    isAsc  升序true 降序 false 默认false
    where 条件数据

    name 查询列
    value 查询值

    op: 数据条件类型:

    like 模糊查询所有
    like1  模糊查询 前部份相似
    like2  模糊查询 后部份相似
    eq 相等
    lt 小于
    le 小于或等于
    gt 大于
    ge 大于或等于

      */

    /**
     * 获取 page
     *
     * @param params
     * @return
     */
    public static IPage getPage(Map<String, Object> params) {
        int pageIndex = Integer.parseInt(params.getOrDefault(PAGE_INDEX_NAME, 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault(PAGE_LIMIT_NAME, 100000).toString());
        return new Page<>(pageIndex, pageSize);
    }

    /**
     * 移除分页的键值
     *
     * @param params
     */
    public static void removePageKey(Map<String, Object> params) {
        if (params.containsKey(PAGE_INDEX_NAME)) {
            params.remove(PAGE_INDEX_NAME);
        }
        if (params.containsKey(PAGE_LIMIT_NAME)) {
            params.remove(PAGE_LIMIT_NAME);
        }

    }

    /**
     * 获取 条件QueryWrapper
     *
     * @param params
     * @return
     */
    public static QueryWrapper getQuery(JSONObject params) {

        JSONArray where = null;
        try {
            where = params.containsKey(PAGE_WHERE) ? params.getJSONArray(PAGE_WHERE) : new JSONArray();
       /* if(!StringHelper.strSqlValid(where))
        {
           // throw new BusinessException("数据非法，请勿再操作！");
            return   new QueryWrapper<>().eq("1",0);
        }
*/
            QueryWrapper wrapper = getQueryWrapper(where);//条件
            String sortField = params.containsKey(PAGE_ORDER_BY_FIELD) ? params.getString(PAGE_ORDER_BY_FIELD) : "";//排序字段
            String sortOrder = params.containsKey(PAGE_IS_ASC) ? params.getString(PAGE_IS_ASC).toString() : "";//desc 降序  asc 升序
            if (!StringHelper.isEmpty(sortField)) {
                sortField = FieldCamelCase(sortField);// 字段驼峰转下划线
                if (sortOrder.toLowerCase().equals("true") || sortOrder.toLowerCase().equals("asc")) {
                    wrapper.orderByAsc(sortField);
                } else {

                    wrapper.orderByDesc(sortField);
                }
            }
           // if (wrapper.getExpression().getNormal().size() == 0)//无条件 查所有
           // {
           //     wrapper.eq("1", 1);
           // }
            if(logicDelete)//逻辑删除
            {
                wrapper.eq(LOGIC_DELETE_KEY, 0);
            }
            return wrapper;
        } catch (JSONException e) {
            // e.printStackTrace();
            log.error("QueryUtil.getQuery出错", e);
            return null;
        }
    }

    public static QueryWrapper getQueryWrapper(JSONArray jsonArray) throws JSONException {
        QueryWrapper wrapper = new QueryWrapper<>();
        if (jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                wrapper = getQwOP(jsonObject.getString("name"), jsonObject.getString("op"), jsonObject.get("value"), wrapper);
            }
        }
        return wrapper;
    }

    public static QueryWrapper getQwOP(String name, String op, Object values, QueryWrapper<HashMap<String, Object>> queryWrapper) {

        if (ComUtil.isEmpty(values)) {
            return queryWrapper;
        }
        name = FieldCamelCase(name);// 字段驼峰转下划线
        //# egion  多字段 模糊查询  如： OwnerName|OwnerCode|BuildingCode|HouseCode_like
        String[] names = name.split("\\|");
        if (names.length > 1) {

            switch (op) {
                case "like"://all
                    switch (names.length) {
                        case 2:
                            queryWrapper.and(wrapper ->
                                    wrapper.like(names[0], values).or().like(names[1], values)
                            );
                            break;
                        case 3:
                            queryWrapper.and(wrapper ->
                                    wrapper.like(names[0], values).or().like(names[1], values).or().like(names[2], values)
                            );
                            break;
                        case 4:
                            queryWrapper.and(wrapper ->
                                    wrapper.like(names[0], values).or().like(names[1], values).or().like(names[2], values).or().like(names[3], values)
                            );
                            break;
                        case 5:
                            queryWrapper.and(wrapper ->
                                    wrapper.like(names[0], values).or().like(names[1], values).or().like(names[2], values).or().like(names[3], values).or().like(names[4], values)
                            );
                            break;
                    }
                    break;
                case "like1":// 前固定

                    switch (names.length) {
                        case 2:
                            queryWrapper.and(wrapper ->
                                    wrapper.likeRight(names[0], values).or().likeRight(names[1], values)
                            );
                            break;
                        case 3:
                            queryWrapper.and(wrapper ->
                                    wrapper.likeRight(names[0], values).or().likeRight(names[1], values).or().likeRight(names[2], values)
                            );
                            break;
                        case 4:
                            queryWrapper.and(wrapper ->
                                    wrapper.likeRight(names[0], values).or().likeRight(names[1], values).or().likeRight(names[2], values).or().likeRight(names[3], values)
                            );
                            break;
                        case 5:
                            queryWrapper.and(wrapper ->
                                    wrapper.likeRight(names[0], values).or().likeRight(names[1], values).or().likeRight(names[2], values).or().likeRight(names[3], values).or().likeRight(names[4], values)
                            );
                            break;
                    }
                    break;
                case "like2"://后固定
                    switch (names.length) {
                        case 2:
                            queryWrapper.and(wrapper ->
                                    wrapper.likeLeft(names[0], values).or().likeLeft(names[1], values)
                            );
                            break;
                        case 3:
                            queryWrapper.and(wrapper ->
                                    wrapper.likeLeft(names[0], values).or().likeLeft(names[1], values).or().likeLeft(names[2], values)
                            );
                            break;
                        case 4:
                            queryWrapper.and(wrapper ->
                                    wrapper.likeLeft(names[0], values).or().likeLeft(names[1], values).or().likeLeft(names[2], values).or().likeLeft(names[3], values)
                            );
                            break;
                        case 5:
                            queryWrapper.and(wrapper ->
                                    wrapper.likeLeft(names[0], values).or().likeLeft(names[1], values).or().likeLeft(names[2], values).or().likeLeft(names[3], values).or().likeLeft(names[4], values)
                            );
                            break;
                    }
                    break;

                case "eq":
                    switch (names.length) {
                        case 2:
                            queryWrapper.and(wrapper ->
                                    wrapper.eq(names[0], values).or().eq(names[1], values)
                            );
                            break;
                        case 3:
                            queryWrapper.and(wrapper ->
                                    wrapper.eq(names[0], values).or().eq(names[1], values).or().eq(names[2], values)
                            );
                            break;
                        case 4:
                            queryWrapper.and(wrapper ->
                                    wrapper.eq(names[0], values).or().eq(names[1], values).or().eq(names[2], values).or().eq(names[3], values)
                            );
                            break;
                        case 5:
                            queryWrapper.and(wrapper ->
                                    wrapper.eq(names[0], values).or().eq(names[1], values).or().eq(names[2], values).or().eq(names[3], values).or().eq(names[4], values)
                            );
                            break;
                    }
                    break;

            }
            return queryWrapper;
         /*   QueryWrapper andQw = new QueryWrapper<>();
            for (int i = 0; i < names.length; i++) {
                andQw = getQwOP(names[i], op, values, andQw);
                if (i != names.length - 1) {
                    andQw.or();
                }
            }
            return queryWrapper.and( andQw);
            */
        } else {
            return getQwSonOP(name, op, values, queryWrapper);
        }

    }

    public static QueryWrapper getQwSonOP(String name, String op, Object values, QueryWrapper wrapper) {

        switch (op) {
            case "like"://all
                wrapper.like(name, values);
                break;
            case "like1":// 前固定
                wrapper.likeRight(name, values);
                break;
            case "like2"://后固定
                wrapper.likeLeft(name, values);
                break;

            case "eq":
                wrapper.eq(name, values);
                break;
            case "lt":
                wrapper.lt(name, values);
                break;


            case "le":
                wrapper.le(name, values);
                break;

            case "gt":
                wrapper.gt(name, values);
                break;


            case "ge":
                wrapper.ge(name, values);
                break;

            case "ne":
                wrapper.ne(name, values);
                break;
            default:
                break;
        }
        return wrapper;
    }


    /**
     * 根据表字段 map 对象 获取 条件QueryWrapper 可包含排序 条件，会自动排除，字段参考 QueryUtil 里说明
     * 多字段 and 条件
     *
     * @param columnMap
     * @return
     */
    public static QueryWrapper getQueryByColumnMap(Map<String, Object> columnMap) {
        QueryWrapper wrapper = null;
        try {
            wrapper = new QueryWrapper<>();
            if (columnMap != null && columnMap.size() > 0) {
                String sortField = "";//排序字段
                String sortOrder = "";//desc 降序  asc 升序
                if (columnMap.containsKey(PAGE_ORDER_BY_FIELD)) {
                    sortField = columnMap.get(PAGE_ORDER_BY_FIELD).toString();
                    columnMap.remove(PAGE_ORDER_BY_FIELD);
                }
                if (columnMap.containsKey(PAGE_IS_ASC)) {
                    sortOrder = columnMap.get(PAGE_IS_ASC).toString();
                    columnMap.remove(PAGE_IS_ASC);
                }
                Iterator<Map.Entry<String, Object>> entries = columnMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, Object> entry = entries.next();
                    wrapper = getQwOP(entry.getKey(), "eq", entry.getValue(), wrapper);
                }
                if(logicDelete)//逻辑删除
                {
                    wrapper.eq(LOGIC_DELETE_KEY, 0);
                }
                if (!StringHelper.isEmpty(sortField)) {
                    sortField = FieldCamelCase(sortField);// 字段驼峰转下划线
                    if (sortOrder.toLowerCase().equals("true") || sortOrder.toLowerCase().equals("asc")) {
                        wrapper.orderByAsc(sortField);
                    } else {

                        wrapper.orderByDesc(sortField);
                    }
                }
            }
            return wrapper;
        } catch (JSONException e) {
            // e.printStackTrace();
            log.error("QueryUtil.getQuery出错", e);
        }
        return wrapper;
    }

    /**
     * map 中字段驼峰转下划线 用于数据库更新字段
     *
     * @param str
     * @return
     */
    public static String FieldCamelCase(String str) {
        if (SystemConstants.DB_FIELD_CAMEL_CASE)//数据库转实体 字段是否转下划线驼峰且首字母小写
        {
            str = StringHelper.underscoreName(str);
        }
        return str;
    }


}