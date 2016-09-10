package com.jiaxy.cache.service;

import com.jiaxy.cache.spring.Namespace;
import com.jiaxy.cache.model.DemoResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author: wutao
 * @version: $Id:DemoService.java 2014/01/22 16:51 $
 *
 */
@Service
public class DemoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Namespace("demoMenuA")
    @Cacheable("demo-cache")
    public DemoResult queryData(String shopId,String date){
        logger.info("==========load from db==================");
        return new DemoResult(shopId,"demodata",date);
    }


    @Namespace("demoMenuA")
    @CachePut("demo-cache")
    public DemoResult updateData(String shopId,String date){
        logger.info("==========update db==================");
        return new DemoResult(shopId,"demodata2",date);
    }

    @Namespace("demoMenuA")
    @CacheEvict("demo-cache")
    public void deleteData(String shopId,String date){
        logger.info("==========delete from db==================");

    }

}
