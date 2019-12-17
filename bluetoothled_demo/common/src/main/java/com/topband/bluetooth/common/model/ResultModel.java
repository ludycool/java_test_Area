package com.topband.bluetooth.common.model;

import com.topband.bluetooth.common.util.DateTimeUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回消息
 */
@Data
public class ResultModel implements Serializable {

    private static final long serialVersionUID = 8037485279569398135L;

    private int status;

    private String message;

    private Object data;

    private long timestamp = DateTimeUtil.getNowTimestamp().getTime();


}
