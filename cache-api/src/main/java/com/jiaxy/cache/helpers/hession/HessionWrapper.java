package com.jiaxy.cache.helpers.hession;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author: wutao
 * @version: $Id:HessionWrapper.java 2014/03/29 15:18 $
 *
 */
public class HessionWrapper {

    private static final Logger logger = LoggerFactory.getLogger(HessionWrapper.class);

    public static byte[] serialize(Object obj)  {
        if(obj==null) {
            return null;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        try {
            ho.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("serailize {} is failed ",obj.getClass());
        }
        return os.toByteArray();
    }


    public static Object deserialize(byte[] bytes)  {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        HessianInput hi = new HessianInput(is);
        try {
            return hi.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("deserialize is failed ");
            return null;
        }
    }

}
