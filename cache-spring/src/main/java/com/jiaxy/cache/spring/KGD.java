package com.jiaxy.cache.spring;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author wutao
 * @version $Id: KGD.java, v1.0 2013/08/05 14:47 $
 *
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface KGD {

    String name();

    String DEFAULT_KGD = "default_kgd";

    String MD5_KGD = "md5_kgd";



}
