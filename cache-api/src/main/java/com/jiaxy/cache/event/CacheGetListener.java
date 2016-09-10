package com.jiaxy.cache.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: wutao
 * @version: $Id:CacheGetListener.java 2014/01/14 15:15 $
 *
 */
public class CacheGetListener implements CacheListener<CacheGetEvent> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onCacheEvent(CacheGetEvent event) {
    }
}
