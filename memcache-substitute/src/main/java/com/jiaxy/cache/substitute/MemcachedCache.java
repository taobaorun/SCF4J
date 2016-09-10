package com.jiaxy.cache.substitute;

import com.jiaxy.cache.AbstractCache;
import com.jiaxy.cache.Cache;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

/**
 * @author: wutao
 * @version: $Id:MemcachedCache.java 2014/01/23 13:05 $
 *
 */
public class MemcachedCache extends AbstractCache implements Cache {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private MemcachedClient client;

    public MemcachedCache(MemcachedClient client, int expire, String name, long opTimeout) {
        this.client = client;
        this.expire = expire;
        this.name = name;
        this.opTimeout = opTimeout;
    }

    public MemcachedCache(MemcachedClient client, int expire, String name) {
        this.client = client;
        this.expire = expire;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getDirect(Object key) {
        Object value = null;
        try {
            value = client.get(key.toString(), opTimeout);
            if ( value != null ){
                try {
                    return new JSONObject(value.toString());
                } catch (JSONException e) {
                    logger.error(" transform value to JSON failed",e);
                }
            }
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (MemcachedException e) {
            logger.error(e.getMessage(), e);
        }
        return value;
    }

    @Override
    public boolean putDirect(Object key, Object value) {
        try {
            if (value instanceof JSONObject){
                value = value.toString();
            }
           return client.set(key.toString(), expire, value, opTimeout);
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (MemcachedException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteDirect(Object key) {
        try {
            return client.delete(key.toString(), opTimeout);
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (MemcachedException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void clear() {
        try {
            client.flushAll(opTimeout);
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (MemcachedException e) {
            logger.error(e.getMessage(), e);
        }
    }


    @Override
    public void setNativeCache(Object nativeCache) {
        client = (MemcachedClient) nativeCache;
    }

    @Override
    public MemcachedClient getNativeCache() {
        return client;
    }
}
