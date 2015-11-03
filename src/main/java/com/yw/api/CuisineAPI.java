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
//		response.getWriter().println("Hello Cuisine");
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

					String hql = "from Contains where ingredients.iid =?and cuisine.cid=?";
					Object[] o = { in.getIid(), cuisine.getCid() };
					Contains contain = (Contains) CuisineServiceInter.uniqueQuery(hql, o);
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
				Integer id = (Integer) jsonobj.get("cid");
				Cuisine c = (Cuisine) CuisineServiceInter.findById(Cuisine.class, id);
				JsonUtil.addSimpleCuisine(res, c);

			} else if ("d".equals(action)) {
				Cuisine cuisine = (Cuisine) JSONObject.toBean(jsonobj, Cuisine.class);
				if (cuisine.getCid() == null) {
					res.put("status", "004");
					res.put("msg", "Incomplete Parameters");
				} else {

					cuisine = (Cuisine) CuisineServiceInter.findById(Cuisine.class, cuisine.getCid());
					java.util.Iterator i = cuisine.getContainses().iterator();
					while (i.hasNext()) {
						CuisineServiceInter.executeDelete(((Contains) i.next()).getIngredients());
					}

					CuisineServiceInter.executeDelete(cuisine);
					res.put("status", "000");
					res.put("msg", "success");
				}
			} else if ("p".equals(action)) {
				String hql = "from Cuisine where users.uid =?";
				Object[] o = {jsonobj.get("uid")};
				List clist = CuisineServiceInter.executeQueryByPage(hql, o, (Integer)jsonobj.get("pageNow"), (Integer)jsonobj.get("pageSize"));
				JSONArray array = new JSONArray();
				JsonUtil.addCuisineList(array, clist);
				res.put("status", "000");
				res.put("msg", "success");
				res.put("pageCount", CuisineServiceInter.getPageCount("select count(*)"+hql, o, (Integer)jsonobj.get("pageSize")));
				res.put("cuisine", array);
			} else {
				res.put("status", "001");
				res.put("msg", "Illegal Parameters");
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
