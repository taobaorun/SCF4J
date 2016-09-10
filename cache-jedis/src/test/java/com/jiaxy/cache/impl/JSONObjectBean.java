/**
 * author wutao
 * copyright www.jiaxy.com
 * 上午9:48:42
 */
package com.jiaxy.cache.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wutao
 *
 */
public class JSONObjectBean {
	
	
	
	public static JSONObject build(){

		JSONObject req = new JSONObject();
		req.put("data1", Arrays.asList("1","2","3","4"));
		Map<String, String> map = new HashMap<String, String>();
		map.put("mapkey1", "1");
		map.put("mapkey2", "2");
		map.put("mapkey3", "3");
		map.put("mapkey4", "4");
		map.put("mapkey5", "5");
		map.put("mapkey6", "6");
		map.put("mapkey7", "7");
		map.put("mapkey8", "8");
		map.put("mapkey9", "9");
		map.put("mapkey10", "10");
		map.put("mapkey11", "11");
		req.put("data2",map );
		req.put("data3",Arrays.asList("1","2")  );
		req.put("data4",Arrays.asList("1","2") );
		JSONObject req2 = new JSONObject();
		req2.put("req2key1", "1");
		req2.put("req2key2", "2");
		req2.put("req2key3", "3");
		req.put("data5", req2);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		req.put("expired", new Date().getTime()+(new Random().nextLong()));
		return req;
	}
	
	
	public static JSONObject buildeResult(){
		JSONObject rs = new JSONObject();
		rs.put("data", Arrays.asList("test"));
		return rs;
	}
	

}
