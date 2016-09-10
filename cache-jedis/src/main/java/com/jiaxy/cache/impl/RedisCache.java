package com.jiaxy.cache.impl;

import redis.clients.jedis.Jedis;

import com.jiaxy.cache.AbstractCache;
import com.jiaxy.cache.Cache;
import com.jiaxy.cache.helpers.hession.HessionWrapper;
import com.jiaxy.cache.helpers.StringUtil;

/**
 * @author: wutao
 * @version: $Id:RedisCache.java 2014/01/13 09:54 $
 *
 */
public class RedisCache extends AbstractCache implements Cache {



    public RedisCache() {
    }

    public RedisCache(Jedis client, int expire, String name, long opTimeout) {
        this.expire = expire;
        this.name = name;
        this.opTimeout = opTimeout;
    }

    public RedisCache(Jedis client, int expire, String name) {
        this.expire = expire;
        this.name = name;
    }

    @Override
    public Object getDirect(Object key) {
    	Jedis client = JedisClientFactory.getJedisClient();
    	try{
    		byte[] data = client.get(String.valueOf(key).getBytes());
            if ( data != null ){
                return HessionWrapper.deserialize(data);
            }else {
                return null;
            }
    	} finally{
    		JedisClientFactory.returnJedis(client);
    	}
        
    }

    @Override
    public boolean putDirect(Object key, Object value) {
        byte[] bytes = HessionWrapper.serialize(value);
        Jedis client = JedisClientFactory.getJedisClient();
        try{
        	 String result = client.set(String.valueOf(key).getBytes(),bytes);
             if ( expire > 0 ){
             	client.expire(String.valueOf(key),expire);
             }
             if (StringUtil.isNotEmpty(result) && "OK".equals(result)){
                 return true;
             } else {
                 return false;
             }
        } finally{
        	JedisClientFactory.returnJedis(client);
        }
       
    }

    @Override
    public boolean deleteDirect(Object key) {
    	 Jedis client = JedisClientFactory.getJedisClient();
        long result = client.expire(String.valueOf(key).getBytes(),0);
        JedisClientFactory.returnJedis(client);
        if ( result > 0 ){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
    }

    @Override
    public void setNativeCache(Object nativeCache) {
    }

    @Override
    public Jedis getNativeCache() {
    	return JedisClientFactory.getJedisClient();
    }
}
