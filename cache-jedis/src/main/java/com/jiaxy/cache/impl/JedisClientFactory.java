package com.jiaxy.cache.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: wutao
 * @version: $JedisClientFactory 2014/01/20 10:09 $
 *
 */
public class JedisClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(JedisClientFactory.class);
    
    private static final Object lock = new Object();

    public static Jedis getJedisClient(){
        return pool.getResource();
    }
    
    public static void returnJedis(Jedis jedis){
    	pool.returnResource(jedis);
    }

    
    static JedisPool pool = null;
    
    static{
    	pool = instance();
    }


    private static JedisPool instance(){
    	if (pool == null ){
    		synchronized (lock) {
    			if (pool == null ){
    				logger.info("-------------------initail pool----------------");
    	    		JedisPoolConfig config = new JedisPoolConfig();   
    	    		config.setMaxTotal(100);
    	            config.setMaxIdle(10);    
    	            config.setMaxWaitMillis(5000);    
    	            config.setTestOnBorrow(false);    
    	            config.setTestOnReturn(false);    
    	            pool = new JedisPool(config, "172.17.18.71");
    			} else {
    				return pool;
    			}
			}
    		
    	}
        return pool;
    }
    
}
