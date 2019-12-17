package com.topband.bluetooth.api.aop;

/**
 * @author ludi
 * @version 1.0
 * @date 2019/10/31 17:10
 * @remark
 */

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.topband.bluetooth.api.common.ServletUtils;
import com.topband.bluetooth.api.config.AppConfig;
import com.topband.bluetooth.common.model.SystemConstants;
import com.topband.bluetooth.common.util.JsonHelper;
import com.topband.bluetooth.common.util.StringHelper;
import com.topband.bluetooth.entity.SysLog;
import com.topband.bluetooth.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * controller 操作日志：切面处理类
 */
@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Autowired
    private SysLogService operationLogService;

    /**
     * 记录方式 db:数据库 ,logfile 日志文件,none:不记录
     */
    private String SaveType()
    {
        return AppConfig.getValueBykey("config.OperationLog.SaveType");
    }

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation( com.topband.bluetooth.api.aop.OperationLog)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @Around(value = "logPoinCut()")
    public Object saveoperationLog(ProceedingJoinPoint joinPoint) throws Throwable {
        try {

            //从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取切入点所在的方法
            Method method = signature.getMethod();

            //获取操作
            OperationLog opLog = method.getAnnotation(OperationLog.class);
            if (opLog != null&&!SaveType().toLowerCase().equals("none")) {//添加注解的 才记录

                HttpServletRequest request = ServletUtils.getRequest();

                // region保存日志
                SysLog operationLog = new SysLog();
                operationLog.setCreateTime(new Date());
                operationLog.setOname(opLog.description());

                // operationLog.setUrl(opLog.modelName() + "/" + opLog.action());
                //获取请求的类名
                // String className = joinPoint.getTarget().getClass().getName();
                //获取请求的方法名
                // String methodName = method.getName();
                //operationLog.setUrl(className + "." + methodName);
                operationLog.setUrl(request.getRequestURI());
                //请求的参数
                Object[] args = joinPoint.getArgs();

                //region将参数所在的数组转换成json
                StringBuilder sb = new StringBuilder();
                boolean isJoint = false;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof JSONObject) {
                        JSONObject parse = (JSONObject) JSONObject.parse(args[i].toString());
                        operationLog.setOcontent(parse.toString());
                    } else if (args[i] instanceof String
                            || args[i] instanceof Long
                            || args[i] instanceof Integer
                            || args[i] instanceof Double
                            || args[i] instanceof Float
                            || args[i] instanceof Byte
                            || args[i] instanceof Short
                            || args[i] instanceof Character) {
                        isJoint = true;
                    } else if (args[i] instanceof String[]
                            || args[i] instanceof Long[]
                            || args[i] instanceof Integer[]
                            || args[i] instanceof Double[]
                            || args[i] instanceof Float[]
                            || args[i] instanceof Byte[]
                            || args[i] instanceof Short[]
                            || args[i] instanceof Character[]) {
                        Object[] strs = (Object[]) args[i];
                        StringBuilder sbArray = new StringBuilder();
                        sbArray.append("[");
                        for (Object str : strs) {
                            sbArray.append(str.toString() + ",");
                        }
                        sbArray.deleteCharAt(sbArray.length() - 1);
                        sbArray.append("]");
                        operationLog.setOcontent(sbArray.toString());
                    } else if (args[i] instanceof Serializable) {
                        try {
                            String rr = JsonHelper.toJson(args[i]);
                            operationLog.setOcontent(rr);

                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            log.error("api log 日志 参数转json出错:" + args[i], e);
                        }

                    } else {
                        continue;
                    }
                }
                if (isJoint) {
                    Map<String, String[]> parameterMap = request.getParameterMap();
                    for (String key : parameterMap.keySet()) {
                        String[] strings = parameterMap.get(key);
                        for (String str : strings) {
                            sb.append(key + "=" + str + "&");
                        }
                    }
                    if (sb.length() > 0)
                        operationLog.setOcontent(sb.deleteCharAt(sb.length() - 1).toString());
                }

                //endregion
                String authorization = request.getHeader(SystemConstants.TOKEN_HEADER);
                if (!StringHelper.isEmpty(authorization)) {//获取用户信息
                    // String userNo = JWTUtil.getUserNo(authorization);
                    //  RmsUser user=ServiceCenter.getUserInfoByToken(authorization);
                    //operationLog.setUserId(user.getId());
                    // operationLog.setUserName(user.getLoginname());
                }

                //获取用户ip地址
                operationLog.setIp(ServletUtils.getIpAndPort(request));
                // 保存
                if (SaveType().toLowerCase().equals("db")) {
                    operationLogService.save(operationLog);
                } else if (SaveType().toLowerCase().equals("logfile")) {
                    log.info("接口请求日志:\r\n" + JsonHelper.toJson(operationLog));
                }
            }

        } catch (Exception e) {

            log.error("操作日志 切面处理类 出错 OperationLogAspect.:saveoperationLog", e);
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }
}
