package com.jiaxy.cache.impl;

import java.io.Serializable;

/**
 * @author: wutao
 * @version: $Id:TestObject.java 2014/01/15 15:18 $
 *
 */
public class TestObject implements Serializable {


    private String key;

    private String name;


    public TestObject() {
    }

    public TestObject(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestObject that = (TestObject) o;

        if (!key.equals(that.key)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
