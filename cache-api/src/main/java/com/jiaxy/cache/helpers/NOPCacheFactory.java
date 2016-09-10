package com.jiaxy.cache.helpers;

import com.jiaxy.cache.AbstractCacheFactory;
import com.jiaxy.cache.Cache;
import com.jiaxy.cache.ICacheFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: wutao
 * @version: $Id:NOPCacheFactory.java 2014/01/10 17:46 $
 *
 */
public class NOPCacheFactory extends AbstractCacheFactory {

    private ICacheFactory cacheFactory;

    @Override
    protected Collection<? extends Cache> loadCaches() {
        List<Cache> cacheList = new ArrayList<Cache>();
        for (String name : cacheFactory.getCacheNames() ){
            cacheList.add(new NOPCache(name));
        }
        return cacheList;
    }

    public NOPCacheFactory(ICacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
        this.init();
    }

    public NOPCacheFactory() {
    }
}
