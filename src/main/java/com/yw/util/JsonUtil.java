package com.yw.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

import com.yw.domain.Contains;
import com.yw.domain.Cuisine;
import com.yw.domain.Ingredients;
import com.yw.domain.Steps;
import com.yw.domain.Type;
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

		Users user = (Users) jsonobj.toBean(jsonobj, Users.class);

		String str = jsonobj.toString();
		System.out.println(str);
		return user;

	}
	
	
	public static String test(){
		
		JSONObject obj = new JSONObject();
		Cuisine c = new Cuisine("尖椒肉丝");
		obj.put("Cuisine", c);
		
		Ingredients i1 = new Ingredients("尖椒");
		Ingredients i2 = new Ingredients("肉丝");
		JSONArray array = new JSONArray();
		array.add(i1);
		array.add(i2);
		obj.put("Ingredients", array);
		array.clear();
		
		Set contains = new  HashSet();
		Contains c1 = new Contains(i1,c,"3个");
		Contains c2 = new Contains(i2,c,"100g");
		contains.add(c1);contains.add(c2);
		array.add(c1);
		array.add(c2);
		obj.put("Contains", array);
		array.clear();

		
		Set step = new HashSet();
		Steps s1 = new Steps(c, "第一步：切");
		Steps s2 = new Steps(c, "第二步：炒");
		step.add(s1);step.add(s2);
		array.add(s1);
		array.add(s2);
		obj.put("Steps", array);
		array.clear();

		
		Set type = new HashSet();
		Type t1 = new Type(c, "川菜");
		Type t2 = new Type(c, "家常菜");
		type.add(t1);type.add(t2);
		array.add(i1);
		array.add(i2);
		obj.put("Type", array);
		array.clear();
		
		return obj.toString();

	}

	public static void addSimpleUser(JSONObject jsonObject, Users user) {
		jsonObject.put("uid", user.getUid());
		jsonObject.put("name", user.getName());
		jsonObject.put("gender", user.getGender());
		jsonObject.put("avatar", user.getAvatar());
		jsonObject.put("bitrhday", new SimpleDateFormat("yyyy-mm-dd").format(user.getBirthday()));
	}

	public static void addSimpleCuisine(JSONObject jsonObject, Cuisine cuisine) {

		jsonObject.put("cid", cuisine.getCid());
		jsonObject.put("name", cuisine.getName());
		jsonObject.put("image", cuisine.getImage());
		JSONObject tmp = new JSONObject();
		addSimpleUser(tmp, cuisine.getUsers());
		jsonObject.put("users", tmp);
		
		JSONArray jsonArray = new JSONArray();
		Iterator i = cuisine.getContainses().iterator();
		while(i.hasNext()){
			JSONObject obj = new JSONObject();
			addSimpleContains(obj, (Contains) i.next());
			jsonArray.add(obj);
		}
		jsonObject.put("containses", jsonArray);
		jsonArray.clear();
		
		
		i = cuisine.getStepses().iterator();
		while(i.hasNext()){
			JSONObject obj = new JSONObject();
			addSimpleSteps(obj, (Steps) i.next());
			jsonArray.add(obj);
		}
		jsonObject.put("stepses", jsonArray);
		jsonArray.clear();
		
//		PriorityQueue<Type> pq = new PriorityQueue<Type>();
//		pq.addAll(cuisine.getTypes());
		
		i = cuisine.getTypes().iterator();
		while(i.hasNext()){
			JSONObject obj = new JSONObject();
			addSimpleTypes(obj, (Type) i.next());
			jsonArray.add(obj);
		}
		jsonObject.put("types", jsonArray);
		
		
		
		
	}
	
	private static void addSimpleTypes(JSONObject jsonObject, Type type) {
		jsonObject.put("tid", type.getTid());
		jsonObject.put("name", type.getName());
	}

	private static void addSimpleSteps(JSONObject jsonObject, Steps step) {
		jsonObject.put("sid", step.getSid());
		jsonObject.put("image", step.getImage());
		jsonObject.put("description", step.getDescription());
	}

	public static void addSimpleContains(JSONObject jsonObject, Contains contains){
		jsonObject.put("iid", contains.getIngredients().getIid());
		jsonObject.put("ingredients", contains.getIngredients().getName());
		jsonObject.put("quantity", contains.getQuantity());
	}

}
