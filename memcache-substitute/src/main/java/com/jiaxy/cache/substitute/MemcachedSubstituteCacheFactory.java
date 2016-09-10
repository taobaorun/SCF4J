package com.jiaxy.cache.substitute;

import com.jiaxy.cache.AbstractCacheFactory;
import com.jiaxy.cache.Cache;
import com.jiaxy.cache.CacheMeta;
import com.jiaxy.cache.SubstituteCacheFactory;
import com.jiaxy.cache.helpers.CacheProxy;
import net.rubyeye.xmemcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: wutao
 * @version: $Id:MemcachedSubstituteCacheFactory.java 2014/01/23 13:01 $
 *
 */
public class MemcachedSubstituteCacheFactory extends AbstractCacheFactory implements SubstituteCacheFactory {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private MemcachedClient memcachedClient;

    @Override
    public boolean isOpen() {
        Object isOpenValue = null;
        try {
            isOpenValue = memcachedClient.get(SubstituteCacheFactory.ISOPEN);
        } catch (Exception e) {
            logger.error("get isOpen flag error",e);
        }
        if ( isOpenValue != null && "true".equalsIgnoreCase(isOpenValue.toString())){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void init() {
        initMemcachedClient();
        super.init();
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        List<CacheMeta> cacheMetaList = getCacheMetas();
        List<Cache> cacheList = new ArrayList<Cache>();
        for (CacheMeta meta : cacheMetaList ){
            cacheList.add(CacheProxy.getProxyCache(new SubstituteCache(new MemcachedCache(memcachedClient, meta.getExpire(), meta.getName(),meta.getOpTimeout()),this),this));
        }
        return cacheList;
    }

    private void initMemcachedClient(){
        memcachedClient =MemcachedClientFactory.build();
    }
}
