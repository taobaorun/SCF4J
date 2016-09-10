package com.jiaxy.cache.spring;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wutao
 * @version: $Id:SpringCacheConfig.java 2014/01/22 16:23 $
 *
 */
@Configuration
@EnableCaching
public class SpringCacheConfig implements CachingConfigurer {


    @javax.annotation.Resource(name = "compass-cache-spring-KeyGenerator")
    private SpringCacheKeyGenerator cacheKeyGenerator;

    @Bean(name = "compass-cache-spring-cacheManager")
    @Override
    public CacheManager cacheManager() {
       return new SpringCacheManager();
    }

    @Bean(name = "compass-cache-spring-keyGenerator")
    @Override
    public KeyGenerator keyGenerator() {
        return cacheKeyGenerator;
    }
}
