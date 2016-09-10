package com.jiaxy.cache.impl;

import com.jiaxy.cache.AbstractKeyManager;
import com.jiaxy.cache.Key;
import com.jiaxy.cache.helpers.hession.HessionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @author: wutao
 * @version: $Id:KeyManager.java 2014/01/20 10:04 $
 *
 */
public class KeyManager extends AbstractKeyManager {


    private Logger logger = LoggerFactory.getLogger(getClass());


    public KeyManager() {

    }

    @Override
    public Collection<Key> getManagedIsolatedKey() {
    	Jedis client = JedisClientFactory.getJedisClient();
    	try{
    		logger.info("-----getManagedIsolatedKey--");
    		long start = System.currentTimeMillis();
            Set<byte[]> isolatedKeyNodes = client.smembers(getKeyspace().getBytes());
           
            Set<Key> isolatedKeySet = new HashSet<Key>();
            List<Key> orderedKeyList = new ArrayList<Key>();
            if ( isolatedKeyNodes == null || isolatedKeyNodes.size() == 0 ){
                return isolatedKeySet;
            }
            int removed = 0;
            for (byte[] keyNodeBytes : isolatedKeyNodes){
                Key key = (Key) HessionWrapper.deserialize(keyNodeBytes);
                if ( !filterExpiredKey(key )){
                    isolatedKeySet.add(key);
                } else{
                    if (client.srem(getKeyspace().getBytes(),keyNodeBytes) > 0  ){
                        removed++;
                    }
                }
            }
            logger.info("delete key size:{}",removed);
            orderedKeyList.addAll(isolatedKeySet);
            Collections.sort(orderedKeyList,new Comparator<Key>() {
                @Override
                public int compare(Key o1, Key o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });
            logger.info("get {} keyspace managed key . size is :{},elaspe :{} ,newest size is :{}", getKeyspace(), orderedKeyList.size(), System.currentTimeMillis() - start,client.smembers(getKeyspace().getBytes()).size());
            return orderedKeyList;
    	} finally {
    		JedisClientFactory.returnJedis(client);
    	}
        
    }

    @Override
    public void addKey(Key key) {
    	Jedis client = JedisClientFactory.getJedisClient();
    	try{
    		byte[] bytes = HessionWrapper.serialize(key);
            //client.getCommands().expire(getKeyspace().getBytes(), 24 * 60 * 60);//1天失效
            long rs = client.sadd(getKeyspace().getBytes(),bytes);
            if ( rs > 0 ){
                logger.debug(" add key {}",key);
            }
    	} finally{
    		JedisClientFactory.returnJedis(client);
    	}
        
        
    }

    @Override
    public void removeKey(Key key) {
    	Jedis client = JedisClientFactory.getJedisClient();
        try{
            byte[] bytes = HessionWrapper.serialize(key);
            long rs = client.srem(getKeyspace().getBytes(), bytes);
            if ( rs > 0 ){
                logger.info(" remove key {} success",key);
            } else {
                logger.error(" remove key {} failed",key);
            }
        }catch (Exception e){
            logger.error(" remove key {} failed",key);
        }finally{
        	JedisClientFactory.returnJedis(client);
        }
    }

    @Override
    public boolean deleteCachedKeys() {
    	Jedis client = JedisClientFactory.getJedisClient();
    	try{
    		Set<byte[]> isolatedKeyNodes = client.smembers(getKeyspace().getBytes());
            if ( isolatedKeyNodes != null && isolatedKeyNodes.size() > 0 ){
                long rs = client.srem(getKeyspace().getBytes(),isolatedKeyNodes.toArray(new byte[isolatedKeyNodes.size()][]));
                if ( rs > 0 ){
                    return true;
                }
            }
            return false;
    	} finally{
    		JedisClientFactory.returnJedis(client);
    	}
        
    }
}
