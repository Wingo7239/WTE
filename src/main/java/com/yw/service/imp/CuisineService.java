package com.yw.service.imp;

import java.io.Serializable;
import java.util.Iterator;

import com.yw.baisc.BasicService;
import com.yw.domain.Contains;
import com.yw.domain.Cuisine;
import com.yw.domain.Ingredients;
import com.yw.domain.Steps;
import com.yw.domain.Type;
import com.yw.domain.Users;
import com.yw.service.inter.CuisineServiceInter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CuisineService extends BasicService implements CuisineServiceInter {

	public Serializable addCuisine(JSONObject jsonobj) {
		// TODO Auto-generated method stub
		Cuisine cuisine = (Cuisine) JSONObject.toBean(jsonobj.getJSONObject("cuisine"), Cuisine.class);
		if (cuisine.getName() == null) {
			throw new NullPointerException();
		}
		cuisine.setUsers(new Users(Integer.parseInt((String) jsonobj.get("uid"))));
		cuisine.setCid((Integer) add(cuisine));

		JSONArray array = jsonobj.getJSONArray("ingredients");
		for (int i = 0; i < array.size(); i++) {
			Ingredients in = new Ingredients((String) array.getJSONObject(i).get("name"));
			if (in.getName() == null)
				throw new NullPointerException();
			Contains contain = new Contains(in, cuisine, (String) array.getJSONObject(i).get("quantity"));
			add(in);
			add(contain);
		}

		array = jsonobj.getJSONArray("type");
		for (int i = 0; i < array.size(); i++) {
			Type type = (Type) JSONObject.toBean(array.getJSONObject(i), Type.class);
			if (type.getName() == null)
				throw new NullPointerException();
			type.setCuisine(cuisine);
			add(type);
		}

		array = jsonobj.getJSONArray("steps");
		for (int i = 0; i < array.size(); i++) {
			Steps step = (Steps) JSONObject.toBean(array.getJSONObject(i), Steps.class);
			if (step.getDescription() == null)
				throw new NullPointerException();
			step.setCuisine(cuisine);
			add(step);
		}
		return cuisine.getCid();
	}

	public void deleteCuisine(JSONObject jsonobj) {
		// TODO Auto-generated method stub

		Cuisine cuisine = (Cuisine) JSONObject.toBean(jsonobj, Cuisine.class);
		if (cuisine.getCid() == null || cuisine.getCid() == 0) {
			throw new NullPointerException();
		} else {

			cuisine = (Cuisine) findById(Cuisine.class, cuisine.getCid());
			java.util.Iterator i = cuisine.getContainses().iterator();
			while (i.hasNext()) {
				executeDelete(((Contains) i.next()).getIngredients());
			}

			executeDelete(cuisine);
		}
		
	}

	public void updateCuisine(JSONObject jsonobj) {
		// TODO Auto-generated method stub
		Cuisine cuisine = (Cuisine) JSONObject.toBean(jsonobj.getJSONObject("cuisine"), Cuisine.class);
		if (cuisine.getName() == null || cuisine.getCid() == null || cuisine.getCid() == 0) {
			throw new NullPointerException();
		}
		cuisine.setUsers(new Users(Integer.parseInt((String) jsonobj.get("uid"))));
		executeUpdate(cuisine);

		JSONArray array = jsonobj.getJSONArray("ingredients");
		for (int i = 0; i < array.size(); i++) {
			Ingredients in = new Ingredients((String) array.getJSONObject(i).get("name"));
			in.setIid((Integer) array.getJSONObject(i).get("iid"));
			if (in.getName() == null)
				throw new NullPointerException();
			Contains contain = new Contains(in, cuisine, (String) array.getJSONObject(i).get("quantity"));
			
			if (in.getIid() == null || in.getIid() == 0) {		
				add(in);
				add(contain);
			} else {
				String hql = "from Contains where ingredients.iid =?and cuisine.cid=?";
				Object[] o = { in.getIid(), cuisine.getCid() };
				contain.setId(((Contains) uniqueQuery(hql, o)).getId());
				executeUpdate(in);
				executeUpdate(contain);
			}
		}

		array = jsonobj.getJSONArray("type");
		for (int i = 0; i < array.size(); i++) {
			Type type = (Type) JSONObject.toBean(array.getJSONObject(i), Type.class);
			if (type.getName() == null)
				throw new NullPointerException();
			type.setCuisine(cuisine);
			if (type.getTid() != null)
				executeUpdate(type);
			else
				add(type);
		}

		array = jsonobj.getJSONArray("steps");
		for (int i = 0; i < array.size(); i++) {
			Steps step = (Steps) JSONObject.toBean(array.getJSONObject(i), Steps.class);
			if (step.getDescription() == null)
				throw new NullPointerException();
			step.setCuisine(cuisine);
			if (step.getSid() != null)
				executeUpdate(step);
			else
				add(step);
		}
	}

}
