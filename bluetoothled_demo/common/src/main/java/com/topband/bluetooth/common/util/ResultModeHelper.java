package com.topband.bluetooth.common.util;

import com.topband.bluetooth.common.enums.CommonError;
import com.topband.bluetooth.common.model.ResultModel0;

import java.util.*;

/**
 * 描述:
 * 返回消息帮助类
 *
 * @author ludycool
 * @create 2018-10-26 9:58
 */
public class ResultModeHelper {

//    public static ResultModel validationFailure(String message) {
//        ResultModel response = new ResultModel();
//        response.setStatus(String.valueOf(ConstantResult.HTTP_STATUS_BAD_REQUEST));
//        response.setCode(ConstantResult.CODE_TOKEN_ERROR);
//        response.setMsg(message);
//        return response;
//    }
//    public static ResultModel tokenTimeOut(String message) {
//        ResultModel response = new ResultModel();
//        response.setStatus(String.valueOf(ConstantResult.HTTP_STATUS_BAD_REQUEST));
//        response.setCode(ConstantResult.CODE_TOKEN_EXPIRE);
//        response.setMsg(message);
//        return response;
//    }
//
//
//    public static ResultModel parameterMissing( String msg) {
//        ResultModel response = new ResultModel();
//        response.setStatus(ConstantResult.PARAMETER_MISSING);
//        response.setCode(ConstantResult.CODE_ERROR);
//        response.setMsg(msg);
//        return response;
//    }

//    public static <T> ResultModel0<T> parameterMissing0(String msg) {
//        ResultModel0 response = new ResultModel0();
//        response.setStatus(ConstantResult.PARAMETER_MISSING);
//        response.setCode(ConstantResult.CODE_ERROR);
//        response.setMsg(msg);
//        return response;
//    }
//    public static <T> ResultModel0<T> parameterError() {
//        ResultModel0 response = new ResultModel0();
//        response.setStatus(ConstantResult.FAILED);
//        response.setCode(ConstantResult.CODE_FAILED);
//        response.setMsg(ConstantResult.PARAM_ERROR);
//        return response;
//    }
//
    public static <T> ResultModel0<T> fail(int status, T data) {
        ResultModel0<T> response = new ResultModel0();
        response.setStatus(status);
        response.setData(data);
        return response;
    }

    public static  ResultModel0 fail(int status) {
        ResultModel0 response = new ResultModel0();
        response.setStatus(status);
        return response;
    }

    public static <T> ResultModel0<T> error(int status,String msg, T data) {
        ResultModel0 response = new ResultModel0();
        response.setStatus(status);
        response.setMessage(msg);
        response.setData(data);
        return response;
    }
//    public static <T> ResultModel0<T> invalid(String msg, T data) {
//        ResultModel0 response = new ResultModel0();
//        response.setStatus(ConstantResult.INVALID_USERNAME_PASSWORD);
//        response.setCode(ConstantResult.CODE_INVALID);
//        response.setData(data);
//        response.setMsg(msg);
//        return response;
//    }
    public static <T> ResultModel0<T> succeed(T data) {
        ResultModel0<T> response = new ResultModel0();
        response.setStatus(CommonError.STATUS_0.getStatus());
        response.setData(data);
        return response;
    }
    public static <T> ResultModel0<T> fail(T data) {
        ResultModel0<T> response = new ResultModel0();
        response.setStatus(CommonError.STATUS_200304.getStatus());
        response.setData(data);
        return response;
    }

    public static <T> ResultModel0<Map<String,Object>> succeedPage(Collection<T> records, long total) {
        ResultModel0<Map<String,Object>> response = new ResultModel0();
        response.setStatus(CommonError.STATUS_0.getStatus());
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("rows",records);
        dataMap.put("total",total);
        response.setData(dataMap);
        return  response;
    }


//    public static <T> ResultModel0<T> failApi(String msg, T data) {
//        ResultModel0 response = new ResultModel0();
//        response.setStatus(ConstantResult.FAILED);
//        response.setCode(ConstantResult.CODE_API_ERROR);
//        response.setData(data);
//        response.setMsg(msg);
//        return response;
//    }
}