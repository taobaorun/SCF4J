package com.jiaxy.cache.helpers;

import com.jiaxy.cache.AbstractCache;
import com.jiaxy.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: wutao
 * @version: $Id:NOPCache.java 2014/01/10 17:43 $
 *
 */
public class NOPCache extends AbstractCache implements Cache {

    public static final String NOP_CACHE_NAME = "NOP_CACHE";

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final NOPCache NOP_CACHE = new NOPCache(NOP_CACHE_NAME);

    public NOPCache(String name) {
        this.name = name;
    }

    @Override
    public Object getDirect(Object key) {
        logger.debug("NOPCache do nothing");
        return null;
    }

    @Override
    public boolean putDirect(Object key, Object value) {
        logger.debug("NOPCache do nothing");
        return false;
    }

    @Override
    public boolean deleteDirect(Object key) {
        logger.debug("NOPCache do nothing");
        return false;
    }

    @Override
    public void clear() {
        logger.debug("NOPCache do nothing");
    }


    @Override
    public Object getNativeCache() {
        return null;
    }
}
