package com.jiaxy.cache.impl;

import com.jiaxy.cache.Cache;
import com.jiaxy.cache.CacheFactory;
import org.junit.Assert;

import java.io.Serializable;


/**
 * @author: wutao
 * @version: $Id:RedisCacheTest.java 2014/01/13 14:33 $
 *
 */

public class RedisCacheTest implements Serializable{


    @org.junit.Test
    public void testPut() throws Exception {
        Cache cache = CacheFactory.getCache("compass_test");
        cache.put("test", new TestObject("testkey","testcompass"));
    }

    @org.junit.Test
    public void testGet() throws Exception {
        Cache cache = CacheFactory.getCache("compass_test");
        Assert.assertEquals( "testcompass",((TestObject)cache.get("test")).getName());


    }



    @org.junit.Test
    public void testEvict() throws Exception {
        Cache cache = CacheFactory.getCache("compass_test");
        cache.delete("test");
        Assert.assertNull(cache.get("test"));
    }

    @org.junit.Test
    public void testGetName() throws Exception {
        Cache cache = CacheFactory.getCache("compass_test");
        Assert.assertEquals("compass_test",cache.getName());
    }


   /* public static void main(String[] args){
        Type[] types = CachePutListener.class.getGenericInterfaces();

        if ( types.length > 0 ){
            for ( Type type : types ){
                if ( type instanceof ParameterizedType){
                    ParameterizedType pt = (ParameterizedType) type;
                    Type tmp = pt.getActualTypeArguments()[0];
                    System.out.println(pt.getOwnerType());
                    System.out.println(pt.getRawType() instanceof Class);
                    if ( tmp instanceof Class ){
                        Class clz = (Class) tmp;
                        System.out.println(clz.getName());
                    }
                }
            }
        }

    }*/
}
