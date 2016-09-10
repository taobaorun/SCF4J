package com.jiaxy.cache.impl;

import com.jiaxy.cache.AbstractCacheFactory;
import com.jiaxy.cache.Cache;
import com.jiaxy.cache.CacheMeta;
import com.jiaxy.cache.MemcachedClientFactory;
import com.jiaxy.cache.helpers.CacheProxy;
import net.rubyeye.xmemcached.MemcachedClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: wutao
 * @version: $Id:MemcachedCacheFactory.java 2014/02/14 13:23 $
 *
 */
public class MemcachedCacheFactory extends AbstractCacheFactory {


    private MemcachedClient memcachedClient = MemcachedClientFactory.build();

    @Override
    protected Collection<? extends Cache> loadCaches() {
        List<CacheMeta> cacheMetaList = getCacheMetas();
        List<Cache> cacheList = new ArrayList<Cache>();
        for (CacheMeta meta : cacheMetaList ){
            cacheList.add(CacheProxy.getProxyCache(new MemcachedCache(memcachedClient, meta.getExpire(), meta.getName(), meta.getOpTimeout()), this));
        }
        return cacheList;
    }
}
