package com.jiaxy.cache;

/**
 * @author: wutao
 * @version: $Id:AbstractCache.java 2014/02/12 10:55 $
 */
public abstract class AbstractCache implements Cache {

    protected int expire;

    protected String name;

    protected long opTimeout;

    @Override
    public Object get(Object key) {
        return getDirect(key);
    }

    @Override
    public boolean put(Object key, Object value) {
        return putDirect(key,value);
    }

    @Override
    public boolean delete(Object key) {
        return deleteDirect(key);
    }

    @Override
    public void setNativeCache(Object nativeCache) {

    }
    @Override
    public void setExpire(int expire) {
        this.expire = expire;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public void setOpTimeout(long opTimeout) {
        this.opTimeout = opTimeout;
    }
    @Override
    public int getExpire() {
        return expire;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public long getOpTimeout() {
        return opTimeout;
    }

}
