package com.yw.api;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.Transaction;

import com.yw.domain.Contains;
import com.yw.domain.Cuisine;
import com.yw.domain.Ingredients;
import com.yw.domain.Steps;
import com.yw.domain.Type;
import com.yw.domain.Users;
import com.yw.service.inter.CuisineServiceInter;
import com.yw.util.JsonUtil;

import javassist.bytecode.Descriptor.Iterator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class CuisineAPI extends GenericServlet implements Servlet {

	private static final long serialVersionUID = 1L;

	private CuisineServiceInter CuisineServiceInter;

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8");
		response.getWriter().println("Hello Cuisine");
		String json = handle((String) request.getParameter("action"), (String) request.getParameter("json"));
		response.getWriter().println(json);
	}

	@SuppressWarnings("finally")
	public String handle(String action, String json) {
		JSONObject jsonobj = JSONObject.fromObject(json);
		JSONObject res = new JSONObject();
		try {
			if ("c".equals(action)) {
				Cuisine cuisine = (Cuisine) JSONObject.toBean(jsonobj.getJSONObject("Cuisine"), Cuisine.class);
				cuisine.setUsers(new Users(Integer.parseInt((String) jsonobj.get("uid"))));
				cuisine.setCid((Integer) CuisineServiceInter.add(cuisine));

				JSONArray array = jsonobj.getJSONArray("Ingredients");
				for (int i = 0; i < array.size(); i++) {
					Ingredients in = new Ingredients((String) array.getJSONObject(i).get("name"));
					Contains contain = new Contains(in, cuisine, (String) array.getJSONObject(i).get("quantity"));
					CuisineServiceInter.add(in);
					CuisineServiceInter.add(contain);
				}

				array = jsonobj.getJSONArray("Type");
				for (int i = 0; i < array.size(); i++) {
					Type type = (Type) JSONObject.toBean(array.getJSONObject(i), Type.class);
					type.setCuisine(cuisine);
					CuisineServiceInter.add(type);
				}

				array = jsonobj.getJSONArray("Steps");
				for (int i = 0; i < array.size(); i++) {
					Steps step = (Steps) JSONObject.toBean(array.getJSONObject(i), Steps.class);
					step.setCuisine(cuisine);
					CuisineServiceInter.add(step);
				}

				res.put("status", "000");
				res.put("msg", "success");

			} else if ("u".equals(action)) {
				Cuisine cuisine = (Cuisine) JSONObject.toBean(jsonobj.getJSONObject("Cuisine"), Cuisine.class);
				cuisine.setUsers(new Users(Integer.parseInt((String) jsonobj.get("uid"))));
				CuisineServiceInter.executeUpdate(cuisine);

				JSONArray array = jsonobj.getJSONArray("Ingredients");
				for (int i = 0; i < array.size(); i++) {
					Ingredients in = new Ingredients((String) array.getJSONObject(i).get("name"));
					in.setIid((Integer) array.getJSONObject(i).get("iid"));
					// todo: correct this hql 
					String hql="from Contains where ingredients =?and cuisine=?";
					Object[] o = {in,cuisine};
					Contains contain = (Contains) CuisineServiceInter.executeQurey(hql,o);
					contain.setIngredients(in);
					if (in.getIid() == null) {
						CuisineServiceInter.add(in);
						CuisineServiceInter.add(contain);
					} else {
						CuisineServiceInter.executeUpdate(in);
						CuisineServiceInter.executeUpdate(contain);
					}
				}

				array = jsonobj.getJSONArray("Type");
				for (int i = 0; i < array.size(); i++) {
					Type type = (Type) JSONObject.toBean(array.getJSONObject(i), Type.class);
					type.setCuisine(cuisine);
					if (type.getTid() != null)
						CuisineServiceInter.executeUpdate(type);
					else
						CuisineServiceInter.add(type);
				}

				array = jsonobj.getJSONArray("Steps");
				for (int i = 0; i < array.size(); i++) {
					Steps step = (Steps) JSONObject.toBean(array.getJSONObject(i), Steps.class);
					step.setCuisine(cuisine);
					if (step.getSid() != null)
						CuisineServiceInter.executeUpdate(step);
					else
						CuisineServiceInter.add(step);
				}

				res.put("status", "000");
				res.put("msg", "success");

			} else if ("r".equals(action)) {
				Cuisine c = (Cuisine) CuisineServiceInter.findById(Cuisine.class, 16);
				JsonUtil.addSimpleCuisine(res, c);

			} else if ("d".equals(action)) {
				Cuisine cuisine = (Cuisine) JSONObject.toBean(jsonobj, Cuisine.class);
				CuisineServiceInter.executeDelete(cuisine);
				res.put("status", "000");
				res.put("msg", "success");
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
			res.clear();
			res.put("status", "999");
			res.put("msg", e.getMessage());
		} finally {
			return res.toString();
		}

	}

	public CuisineServiceInter getCuisineServiceInter() {
		return CuisineServiceInter;
	}

	public void setCuisineServiceInter(CuisineServiceInter cuisineServiceInter) {
		CuisineServiceInter = cuisineServiceInter;
	}

}
