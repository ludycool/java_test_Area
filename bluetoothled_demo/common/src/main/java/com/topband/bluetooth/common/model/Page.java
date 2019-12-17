package com.topband.bluetooth.common.model;

import lombok.Data;

import java.util.List;

/**
 * 分页 vo
 * @param <T>
 */
@Data
public class Page<T> {

    /**
     *记录总数
     */
  private   long total;
    /**
     * 每页数据量
     */
    private  long size;
    /**
     * 当前页码
     */
    private   long  current;
    /**
     * 总页数
     *
     */
    private   long  pages;

    /**
     * 当前页 数据
     */
    private List<T> records;

}
