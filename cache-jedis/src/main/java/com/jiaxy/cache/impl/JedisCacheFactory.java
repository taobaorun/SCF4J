package com.jiaxy.cache.impl;

import com.jiaxy.cache.AbstractCacheFactory;
import com.jiaxy.cache.Cache;
import com.jiaxy.cache.CacheMeta;
import com.jiaxy.cache.helpers.CacheProxy;
import com.jiaxy.cache.helpers.StringUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: wutao
 * @version: $Id:JedisCacheFactory.java 2014/01/10 16:26 $
 *
 */
public class JedisCacheFactory extends AbstractCacheFactory {


    @Override
    protected Collection<? extends Cache> loadCaches() {
        Jedis client = null;
        try{
            client = JedisClientFactory.getJedisClient();
            List<CacheMeta> cacheMetaList = getCacheMetas();
            List<Cache> cacheList = new ArrayList<Cache>();
            for (CacheMeta meta : cacheMetaList ){
                if (StringUtil.isNotEmpty(meta.getClassName())){
                    cacheList.add(CacheProxy.getProxyCache(instantiateCustomCache(client,meta),this));
                } else {
                    cacheList.add(CacheProxy.getProxyCache(new RedisCache(client,meta.getExpire(),meta.getName()),this));
                }
            }
            return cacheList;
        } finally {
            JedisClientFactory.returnJedis(client);
        }

    }

}
