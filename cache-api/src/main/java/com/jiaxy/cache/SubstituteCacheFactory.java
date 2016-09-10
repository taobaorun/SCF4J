package com.jiaxy.cache;

/**
 * @author: wutao
 * @version: $Id:SubstituteCacheFactory.java 2014/01/23 11:17 $
 *
 */
public interface SubstituteCacheFactory extends ICacheFactory {


    String ISOPEN = "isOpen";

    /**
     * 缓存切换开关是否打开
     *
     * @return
     */
    boolean isOpen();
}
