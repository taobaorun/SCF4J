package com.jiaxy.cache.event;

import com.jiaxy.cache.ICacheFactory;

/**
 * @author: wutao
 * @version: $Id:CacheClearEvent.java 2014/01/14 15:10 $
 *
 */
public class CacheClearEvent extends CacheEvent{

    public CacheClearEvent(ICacheFactory cacheFactory) {
        super(cacheFactory);
    }
}
