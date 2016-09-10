package com.jiaxy.cache.spi;

import com.jiaxy.cache.ICacheFactory;

/**
 * @author: wutao
 * @version: $Id:CacheFactoryBinder.java 2014/01/17 15:54 $
 *
 */
public interface CacheFactoryBinder {

    ICacheFactory getCacheFactory();

    String getCacheFactoryClassStr();

}
