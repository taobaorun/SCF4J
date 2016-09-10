package com.jiaxy.cache;

import com.jiaxy.cache.event.CacheEventPublisher;

import java.util.Collection;

/**
 * @author: wutao
 * @version: $Id:ICacheFactory.java 2014/01/09 18:20 $
 *
 */
public interface ICacheFactory extends CacheEventPublisher {

    Cache getCache(String name);

    Collection<String> getCacheNames();

    void init();

}
