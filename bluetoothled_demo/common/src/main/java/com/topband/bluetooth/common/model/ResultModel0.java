package com.topband.bluetooth.common.model;


import com.topband.bluetooth.common.util.DateTimeUtil;
import lombok.Data;

@Data
public  class ResultModel0<T> {
    private static final long serialVersionUID = 8037485279569398135L;

    private int status;

    private String message;

    private T data;

    private long timestamp = DateTimeUtil.getNowTimestamp().getTime();
}
