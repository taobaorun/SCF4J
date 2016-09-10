package com.jiaxy.cache;

import com.jiaxy.cache.helpers.StringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author: wutao
 * @version: $Id:CacheMeta.java 2014/01/24 15:22 $
 *
 */
public class CacheMeta {

    private String name;

    private int expire;

    private long opTimeout;

    /**
     * extend
     *
     * cache implementation .ie com.jiaxy.cache.RedisCache
     */
    private String className;

    private boolean isSetOpTimeout = false;

    public CacheMeta(String name, int expire, long opTimeout) {
        this.name = name;
        this.expire = expire;
        this.opTimeout = opTimeout;

        this.isSetOpTimeout = true;
    }

    public CacheMeta(String name, int expire) {
        this(name, expire, 0);
        this.isSetOpTimeout = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public long getOpTimeout() {
        return opTimeout;
    }

    public void setOpTimeout(long opTimeout) {
        this.opTimeout = opTimeout;
    }

    public boolean isSetOpTimeout() {
        return isSetOpTimeout;
    }

    public void setSetOpTimeout(boolean setOpTimeout) {
        isSetOpTimeout = setOpTimeout;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 解析配置文件
     *
     * @param in 配置文件的输入流
     * @return 各逻辑cache的细粒度参数
     */
    public static List<CacheMeta> parseCacheMetas(InputStream in) {
        CacheMeta defaultMeta = new CacheMeta(Cache.DEFAULT_CACHE,24 * 60 * 60 ,5000);
        if ( in == null ){
            return Arrays.asList(defaultMeta);
        }
        List<CacheMeta> configurations = new ArrayList<CacheMeta>();
        configurations.add(defaultMeta);
        CacheMeta config;

        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(in);
        } catch (DocumentException e) {
            throw new RuntimeException("初始化缓存配置文件出错",e);
        }
        Element root = doc.getRootElement();
        Element cache;

        String name, value;
        int expire;
        List<String> existedNames = new ArrayList<String>();

        for (Iterator<Element> iterator = root.elementIterator("cache"); iterator
                .hasNext(); ) {
            cache = iterator.next();
            name = cache.attributeValue("name").trim();

            if (existedNames.contains(name)) {
                throw new IllegalArgumentException(String.format(
                        "配置文件中配置了同名[%s]的cache，初始化缓存失败！", name));
            }

            expire = Integer
                    .parseInt(cache.attributeValue("expire").trim());
            value = cache.attributeValue("opTimeout");

            existedNames.add(name);



            if (value != null) {
                config = new CacheMeta(name, expire,
                        Long.parseLong(value.trim()));
            } else {
                config = new CacheMeta(name, expire);
            }

            if (StringUtil.isNotEmpty(cache.attributeValue("class"))){
                config.setClassName(cache.attributeValue("class"));
            }
            configurations.add(config);
        }

        return configurations;
    }

}
