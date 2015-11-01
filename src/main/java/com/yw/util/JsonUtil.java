package com.yw.util;

import com.yw.domain.Users;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class JsonUtil {

	public static Users parseUser(String json) {
		
		JSONObject jsonobj = new JSONObject();
		
		jsonobj.put("birthday", "2010-01-01");
		jsonobj.put("gender", "Male");
		jsonobj.put("name", "O2O");
		jsonobj.put("password", "123");
		jsonobj.put("uid", "12");
		
		Users user = (Users) jsonobj.toBean(jsonobj,Users.class);
		
		String str = jsonobj.toString();
		System.out.println(str);
		return user;

	}
	
	
}
