package com.jiaxy.cache;

import com.jiaxy.cache.helpers.NOPCacheFactory;
import com.jiaxy.cache.helpers.StringUtil;
import com.jiaxy.cache.impl.StaticCacheBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @author: wutao
 *
 * @version: $Id:CacheFactory.java 2014/01/10 15:50 $
 *
 */
public final class CacheFactory {

    private static final Logger logger = LoggerFactory.getLogger(CacheFactory.class);

    private static String STATIC_CACHE_BINDER_PATH = "com/jiaxy/cache/impl/StaticCacheBinder.class";

    static final int UNINITIALIZED = 0;

    static final int ONGOING_INITIALIZATION = 1;

    static final int FAILED_INITIALIZATION = 2;

    static final int SUCCESSFUL_INITIALIZATION = 3;

    static final int NOP_FALLBACK_INITIALIZATION = 4;

    static int INITIALIZATION_STATE = UNINITIALIZED;

   private static final ICacheFactory NOP_FACTORY = new NOPCacheFactory();

   private static ICacheFactory NOP_FACTORY_DELEGATOR ;

   private static SubstituteCacheFactory substituteCacheFactory;


    private static final Properties config = new Properties();


    static {
        initConfig();
    }

    private final static void performInitialization() {
        bind();
    }

    private final static void bind() {
        try{
            Set staticCacheBinderPathSet = findPossibleStaticLoggerBinderPathSet();
            reportMultipleBindingAmbiguity(staticCacheBinderPathSet);
            StaticCacheBinder.getSingleton();
            INITIALIZATION_STATE = SUCCESSFUL_INITIALIZATION;
            reportActualBinding(staticCacheBinderPathSet);


        }catch (NoClassDefFoundError e){
            String msg = e.getMessage();
            if (messageContainsCompassCacheImplStaticCacheBinder(msg)) {
                INITIALIZATION_STATE = NOP_FALLBACK_INITIALIZATION;
                logger.error("Failed to load class \"StaticCacheBinder\".\"");
            } else {
                failedBinding(e);
                throw e;
            }

        }catch (Exception e){
            failedBinding(e);
            throw new IllegalStateException("Unexpected initialization failure", e);
        }

    }

    private static void initConfig(){
        InputStream in = CacheFactory.class.getClassLoader().getResourceAsStream("cache/cache-api.properties");
        if ( in == null ){
            in = Thread.currentThread().getClass().getClassLoader().getResourceAsStream("cache/cache-api.properties");
        }
        try {
            config.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("read cache-api.properties failed ",e);
        }
    }
    static void failedBinding(Throwable t) {
        INITIALIZATION_STATE = FAILED_INITIALIZATION;
        logger.error("Failed to instantiate CompassCache CacheFactory",t);
    }

    private static Set findPossibleStaticLoggerBinderPathSet() {
        Set staticCacheBinderPathSet = new LinkedHashSet();
        try {
            ClassLoader cacheFactoryClassLoader = CacheFactory.class.getClassLoader();
            Enumeration paths;
            if (cacheFactoryClassLoader == null) {
                paths = ClassLoader.getSystemResources(STATIC_CACHE_BINDER_PATH);
            } else {
                paths = cacheFactoryClassLoader.getResources(STATIC_CACHE_BINDER_PATH);
            }
            while ( paths.hasMoreElements() ){
                URL path = (URL) paths.nextElement();
                staticCacheBinderPathSet.add(path);
            }
        } catch (IOException e) {
            logger.error("Error getting resources from path",e);
        }
        return staticCacheBinderPathSet;
    }

    private static void reportActualBinding(Set staticCacheBinderPathSet) {
        if (isAmbiguousStaticCacheBinderPathSet(staticCacheBinderPathSet)) {
            logger.info("*** Actual binding is of type [{}]",StaticCacheBinder.getSingleton().getCacheFactoryClassStr());
        }
    }

    private static void reportMultipleBindingAmbiguity(Set staticCacheBinderPathSet) {
        if (isAmbiguousStaticCacheBinderPathSet(staticCacheBinderPathSet)) {
            logger.info("Class path contains multiple compass-cache bindings.");
            Iterator iterator = staticCacheBinderPathSet.iterator();
            while (iterator.hasNext()) {
                URL path = (URL) iterator.next();
                logger.info("Found binding in [{}]", path);
            }
        }
    }

    private static boolean messageContainsCompassCacheImplStaticCacheBinder(String msg) {
        if (msg == null)
            return false;
        if (msg.indexOf("com/jiaxy/compass/cache/impl/StaticCacheBinder") != -1)
            return true;
        if (msg.indexOf("StaticCacheBinder") != -1)
            return true;
        return false;
    }

    private static boolean isAmbiguousStaticCacheBinderPathSet(Set staticCacheBinderPathSet) {
        return staticCacheBinderPathSet.size() > 1;
    }

    public static Cache getCache(String name){
        ICacheFactory iCacheFactory = getICacheFactory();
        return iCacheFactory.getCache(name);
    }



    public static ICacheFactory getICacheFactory() {
        return getICacheFactory(true);
    }


    /**
     * if  substitute  is true ,ICacheFactory supports SubstituteCacheFactory.
     * @param  substitute
     * @return
     */
    public static ICacheFactory getICacheFactory(boolean  substitute) {
        if (INITIALIZATION_STATE == UNINITIALIZED) {
            INITIALIZATION_STATE = ONGOING_INITIALIZATION;
            performInitialization();
        }
        switch (INITIALIZATION_STATE) {
            case SUCCESSFUL_INITIALIZATION:
                if ( Boolean.valueOf(config.getProperty("isCache","true")) &&  substitute && Boolean.valueOf(config.getProperty("isSubstitute", "false"))){
                    if ( substituteCacheFactory == null ){
                        substituteCacheFactory = loadSubstituteCacheFactory();
                    }
                    substituteCacheFactory.init();
                    logger.debug("{} SubstituteCacheFactory is opened ",substituteCacheFactory.getClass().getName());
                    return substituteCacheFactory;
                }
                ICacheFactory cacheFactory = StaticCacheBinder.getSingleton().getCacheFactory();
                if ( !Boolean.valueOf(config.getProperty("isCache","true")) || StringUtil.isEmpty(config.getProperty("keyspace"))){
                    if ( StringUtil.isEmpty(config.getProperty("keyspace") )){
                        logger.error("keyspace in cache-api.properties is empty,cache will not be used ");
                    } else {
                        logger.info("*** cache is turned off");
                    }
                    if ( NOP_FACTORY_DELEGATOR == null ){
                        NOP_FACTORY_DELEGATOR =  new NOPCacheFactory(cacheFactory);
                    }
                    return NOP_FACTORY_DELEGATOR;
                }

                return cacheFactory;
            case NOP_FALLBACK_INITIALIZATION:
                return NOP_FACTORY;
            case FAILED_INITIALIZATION:
                throw new IllegalStateException("failed initialization");
            case ONGOING_INITIALIZATION:
                return NOP_FACTORY;
        }
        throw new IllegalStateException("Unreachable code");
    }

    public static Properties getConfig(){
        return config;
    }


    public static String getCacheConfigValue(String p,String defaultValue){
        return config.getProperty(p,defaultValue);
    }

    private static SubstituteCacheFactory loadSubstituteCacheFactory(){
        if (StringUtil.isEmpty(config.getProperty("SubstituteCacheFactoryClass"))){
            return new NOPSubstituteCacheFactory();
        } else {
            try {
                Class<SubstituteCacheFactory> substituteCacheFactoryClass = (Class<SubstituteCacheFactory>) Class.forName(config.getProperty("SubstituteCacheFactoryClass"));
                return substituteCacheFactoryClass.newInstance();
            } catch (ClassNotFoundException e) {
                logger.info("*** load {} SubstituteCacheFactory failed,will use NOPSubstituteCacheFactory",config.getProperty("SubstituteCacheFactoryClass"));
                return new NOPSubstituteCacheFactory();
            } catch (InstantiationException e) {
                logger.info("*** Instantiate {} SubstituteCacheFactory failed,will use NOPSubstituteCacheFactory",config.getProperty("SubstituteCacheFactoryClass"));
                return new NOPSubstituteCacheFactory();
            } catch (IllegalAccessException e) {
                logger.info("*** Instantiate {} SubstituteCacheFactory failed,will use NOPSubstituteCacheFactory",config.getProperty("SubstituteCacheFactoryClass"));
                return new NOPSubstituteCacheFactory();
            }
        }
    }

    static class NOPSubstituteCacheFactory extends AbstractCacheFactory implements SubstituteCacheFactory{

        @Override
        protected Collection<? extends Cache> loadCaches() {
            return null;
        }

        @Override
        public boolean isOpen() {
            return false;
        }
    }

}
