package com.jiaxy.cache.spring;

import java.lang.annotation.*;

/**
 * parameter will not be used as cache key part
 *
 * @author: wutao
 * @version: $Id:ExcludeAsKeyPart.java 2014/03/25 16:06 $
 *
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ExcludeAsKeyPart {
}
