package com.jiaxy.cache.spring;


import com.jiaxy.cache.helpers.StringUtil;

import java.lang.reflect.Method;

/**
 * @author wutao
 * @version $Id: DefaultKeyGeneratorDelegate.java, v1.0 2013/08/05 14:05 $
 *
 */

@KGD(name = KGD.MD5_KGD)
public class MD5KeyGeneratorDelegate implements KeyGeneratorDelegate {

    public static final int NO_PARAM_KEY = 0;

    public static final int NULL_PARAM_KEY = 53;



    @Override
    public Object generate(Object target, Method method, Object... params) {
        final String declare = target.getClass().getName() ;
        String key;
        if (params.length == 1) {
            if (params[0] == null) {
                key = declare + NULL_PARAM_KEY;
            } else {
                key = declare + params[0];
            }
        } else if (params.length == 0) {
            key = declare + NO_PARAM_KEY;
        } else {
            int hashCode = 17;
            for (Object object : params) {
                hashCode = 31 * hashCode
                        + (object == null ? NULL_PARAM_KEY : object.hashCode());
            }

            key = declare + hashCode;
        }
        return StringUtil.md5(key);
    }
}
