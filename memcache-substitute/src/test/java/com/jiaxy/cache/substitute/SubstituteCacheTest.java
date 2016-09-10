package com.jiaxy.cache.substitute;

import com.jiaxy.cache.helpers.CacheUtil;
import junit.framework.Assert;

/**
 * @author: wutao
 * @version: $Id:SubstituteCacheTest.java 2014/02/12 10:11 $
 *
 */
public class SubstituteCacheTest {


    @org.junit.Test
    public void testPut() throws Exception {
        boolean rs =  CacheUtil.put("compass_test","testSubsitituteKey1","testSubsitituteValue1");
        Assert.assertEquals(true, rs);
        Thread.sleep(1000);
    }

    @org.junit.Test
    public void testGet() throws Exception {
        Assert.assertEquals("testSubsitituteValue1", CacheUtil.get("compass_test","testSubsitituteKey1"));
    }



    @org.junit.Test
    public void testDelete() throws Exception {
        boolean rs =  CacheUtil.delete("compass_test","testSubsitituteKey1");
        Assert.assertEquals(true, rs);
        Thread.sleep(1000);
    }
}
