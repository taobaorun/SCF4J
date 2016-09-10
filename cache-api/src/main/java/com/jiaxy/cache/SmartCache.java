package com.jiaxy.cache;

/**
 * for extend
 *
 * @author: wutao
 * @version: $Id:SmartCache.java 2014/01/24 15:20 $
 *
 */
public interface SmartCache extends Cache{

    /**
     * get CacheMeta information do what you want to do
     *
     *
     * @param meta meta
     * @see Cache#getNativeCache()
     */
    void fillCacheMetaValue(CacheMeta meta);
}
