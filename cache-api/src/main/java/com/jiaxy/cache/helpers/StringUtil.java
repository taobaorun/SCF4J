package com.jiaxy.cache.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: wutao
 * @version: $Id:StringUtil.java 2014/01/10 15:01 $
 *
 */
public class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static boolean isEmpty(String value) {
        if (value == null)
            return true;
        else if (value.trim().equalsIgnoreCase("")
                || value.trim().equalsIgnoreCase("null"))
            return true;
        else
            return false;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * MD5
     */
    public static String md5(String str) {
        if (str == null) {
            return str;
        }
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException caught!",e);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException caught!",e);
        }
        StringBuffer md5StrBuff = new StringBuffer("");
        if(messageDigest!=null){
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                    md5StrBuff.append("0").append(
                            Integer.toHexString(0xFF & byteArray[i]));
                else
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return "d" + md5StrBuff.toString();
    }

}
