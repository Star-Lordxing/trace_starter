package com.dubbo.trace.configuration;

import java.lang.annotation.*;

/**
 * @author 王柱星
 * @version 1.0
 * @title
 * @time 2018年10月25日
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableTraceAutoConfigurationProperties {
}
