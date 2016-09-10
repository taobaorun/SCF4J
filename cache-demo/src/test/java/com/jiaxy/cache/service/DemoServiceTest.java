package com.jiaxy.cache.service;

import com.jiaxy.cache.model.DemoResult;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: wutao
 * @version: $Id:DemoServiceTest.java 2014/01/22 17:18 $
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/demo_context.xml","classpath:/cache/cache_AppContext.xml"})
public class DemoServiceTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void testDeleteData() throws Exception {
        demoService.deleteData("11001", "2014-01-22");

    }

    @Test
    public void testQueryData() throws Exception {
        DemoResult result = demoService.queryData("11001", "2014-01-22");
        Assert.assertEquals("11001",result.getShopId());
        Assert.assertEquals("2014-01-22",result.getDate());
        Assert.assertEquals("demodata",result.getData());
//        Thread.sleep(1000);
    }

}
