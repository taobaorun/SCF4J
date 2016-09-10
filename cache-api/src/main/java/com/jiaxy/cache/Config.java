package com.jiaxy.cache;

/**
 * @author: wutao
 * @version: $Id:Config.java 2014/03/31 17:09 $
 *
 */
public interface Config {


    //cache switch
    String ISCACHE = "isCache";

    // is support substitute cache ,default is false
    String ISSUBSTITUE = "isSubstitute";

    //keyspace is for managing the cache key
    String KEYSPACE = "keyspace";

    // if isSubstitue is true ,this property must set
    String SUBSTITUTECACHEFACTORYCLASS = " SubstituteCacheFactoryClass";

    //thread config
    String KEYMANAGER_MANAGEKEY_THREAD = "keymanager_managekey_thread";

    //clear expired key thread num
    String KEYMANAGER_CLEARKEY_THREAD = "keymanager_clearkey_thread";
    //clear expired key thread schedule period (unit is second)
    String KEYMANAGER_CLEARKEY_PERIOD = "keymanager_clearkey_period";
}
