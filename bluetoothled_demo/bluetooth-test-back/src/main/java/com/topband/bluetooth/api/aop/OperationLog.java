package com.topband.bluetooth.api.aop;

import java.lang.annotation.*;

/**
 * @author ludi
 * @version 1.0
 * @date 2019/10/31 17:03
 * @remark
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface OperationLog {
    /**
     * 模块名称
     */
   // String modelName() default "";

    /**
     * 操作
     */
    //String action()default "";
    /**
     * 描述.
     */
    String description() default "";
}
