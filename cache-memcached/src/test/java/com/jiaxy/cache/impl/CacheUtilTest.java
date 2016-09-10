package com.jiaxy.cache.impl;

import com.jiaxy.cache.helpers.CacheUtil;
import junit.framework.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;

/**
 * @author: wutao
 * @version: $Id:CacheUtilTest.java 2014/01/21 15:32 $
 *
 */
public class CacheUtilTest {

    @Test
    public void testPut() throws Exception {

        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("args1","testArg1");
        params.put("args2","testArg2");
        CacheUtil.put("compass_test","A-1-1","com.jiaxy.cache.impl.testPut",params,"testCache");
        Thread.sleep(1000);
    }

    @Test
    public void testGet() throws Exception {
        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("args1","testArg1");
        params.put("args2","testArg2");
        Assert.assertEquals("testCache",CacheUtil.get("compass_test","A-1-1","com.jiaxy.cache.impl.testPut",params));
        Thread.sleep(1000);

    }

    @Test
    public void testDelete() throws Exception {
        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("args1","testArg1");
        params.put("args2","testArg2");
        CacheUtil.delete("compass_test","A-1-1","com.jiaxy.cache.impl.testPut",params);
        Assert.assertNull(CacheUtil.get("compass_test","A-1-1","com.jiaxy.cache.impl.testPut",params));
        Thread.sleep(1000);
    }

    @Test
    public void testPutStringKey() throws Exception {
        CacheUtil.put("compass_test","testStringKey1","testStringKeyValue1");
        CacheUtil.put("compass_test","testStringKey2","testStringKeyValue2");
        CacheUtil.put("compass_test","testStringKey3","testStringKeyValue3");
        CacheUtil.put("compass_test","testStringKey4","testStringKeyValue4");
        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("args1","11001");
        params.put("args2","11002");
        CacheUtil.put("compass_test","testStringKey1","com.jiaxy.cache.impl.testPut",params,"testStringKeyValue1-1");
        Thread.sleep(100);
    }

    @Test
    public void testGetStringKey() throws Exception {
        Assert.assertEquals("testStringKeyValue1", CacheUtil.get("compass_test", "testStringKey1"));

    }
    @Test
    public void testDefaultCacheNamePutStringKey() throws Exception {
        CacheUtil.put("testDefaultCacheNameStringKey1","testStringKeyValue1");
        Thread.sleep(1000);
    }

    @Test
    public void testDefaultCacheNameGetStringKey() throws Exception {
        Assert.assertEquals("testStringKeyValue1", CacheUtil.get( "testDefaultCacheNameStringKey1"));
        Thread.sleep(1000);

    }

    @Test
    public void testPutWithNamespace() throws Exception {
        CacheUtil.putWithNamespace("testWithNamespace-compass-jiaxy","testStringKey1","testStringKeyValue1");
        CacheUtil.putWithNamespace("testWithNamespace-compass-jiaxy","testStringKey2","testStringKeyValue2");
        CacheUtil.putWithNamespace("testWithNamespace-compass-jiaxy", "testStringKey3", "testStringKeyValue3");
        Thread.sleep(1000);
    }


    @Test
    public void testGetWithNamespace() throws Exception {
        Assert.assertEquals("testStringKeyValue1", CacheUtil.getWithNamespace("testWithNamespace-compass-jiaxy", "testStringKey1"));
    }

    @Test
    public void testDeleteWithNamespace() throws Exception {
        CacheUtil.deleteWithNamespace("testWithNamespace-compass-jiaxy","testStringKey1");
        Assert.assertNull(CacheUtil.getWithNamespace("testWithNamespace-compass-jiaxy", "testStringKey1"));
        Thread.sleep(100);
    }

    @Test
    public void testDeleteStringKey() throws Exception {
        CacheUtil.delete("compass_test","testStringKey1");
        Thread.sleep(1000);
        Assert.assertNull(CacheUtil.get("compass_test", "testStringKey1"));
    }


    public static void main(String[] args){
        CacheUtil.put("ssss","测试");
        System.out.println(CacheUtil.get("ssss"));
    }
}
