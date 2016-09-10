package com.jiaxy.cache.spring;

import com.jiaxy.cache.helpers.KeyNodeGenerator;
import com.jiaxy.cache.helpers.StringUtil;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * @author: wutao
 * @version: $Id:DefaultKeyGeneratorDelegate.java 2014/01/22 14:27 $
 *
 */
@KGD( name = KGD.DEFAULT_KGD)
public class DefaultKeyGeneratorDelegate implements KeyGeneratorDelegate{

    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public Object generate(Object target, Method method, Object... params) {

        Namespace namespace;
        if ( target.getClass().getAnnotation(Namespace.class) != null ){
            namespace = target.getClass().getAnnotation(Namespace.class);
        } else {
            namespace = method.getAnnotation(Namespace.class);
        }
        String namespaceValue;
        if ( namespace != null && StringUtil.isNotEmpty(namespace.value())){
            namespaceValue = namespace.value();
        } else {
            namespaceValue = target.getClass().getPackage().getName();
        }
        LinkedHashMap<String,Object> paramsMap = new LinkedHashMap<String,Object>();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        Annotation[][] paramsAnnotations = method.getParameterAnnotations();
        if ( parameterNames != null && params != null && parameterNames.length == params.length){
            for ( int i = 0 ;i < parameterNames.length ;i++ ){
               if ( paramsAnnotations[i].length > 0 ){
                   if ( paramsAnnotations[i][0] instanceof ExcludeAsKeyPart){
                       continue;
                   }
               }
                paramsMap.put(parameterNames[i],params[i]);
            }
        }else {
            if ( params != null ){
                for ( int i = 0 ;i < params.length ;i++ ){
                    if ( paramsAnnotations[i].length > 0 ){
                        if ( paramsAnnotations[i][0] instanceof ExcludeAsKeyPart){
                            continue;
                        }
                    }
                    paramsMap.put("args"+i,params[i]);
                }
            }
        }
        return KeyNodeGenerator.generateKey(namespaceValue,target.getClass().getPackage().getName()+"."+method.getName(),paramsMap);
    }

}
