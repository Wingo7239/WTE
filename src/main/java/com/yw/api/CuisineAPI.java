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
import org.springframework.transaction.annotation.Transactional;

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
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class CuisineAPI extends GenericServlet implements Servlet {

	private static final long serialVersionUID = 1L;

	private CuisineServiceInter CuisineServiceInter;

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8");
		// response.getWriter().println("Hello Cuisine");
		
		
		String json = handle((String) request.getParameter("action"), (String) request.getParameter("json"));

		response.getWriter().println(json);
	}

	

	@SuppressWarnings("finally")
	@Transactional
	public String handle(String action, String json) {
		JSONObject jsonobj = JSONObject.fromObject(json);
		JSONObject res = new JSONObject();
		try {

			if ("c".equals(action)) {
				int cid = (Integer) CuisineServiceInter.addCuisine(jsonobj);
				res.put("status", "000");
				res.put("msg", "success");
				res.put("cid", cid);

			} else if ("u".equals(action)) {
				CuisineServiceInter.updateCuisine(jsonobj);

				res.put("status", "000");
				res.put("msg", "success");

			} else if ("r".equals(action)) {
				if(!jsonobj.containsKey("cid"))
					throw new NullPointerException();
				Cuisine c = (Cuisine) CuisineServiceInter.findById(Cuisine.class, (Integer) jsonobj.get("cid"));
				JsonUtil.addSimpleCuisine(res, c);

			} else if ("d".equals(action)) {
				CuisineServiceInter.deleteCuisine(jsonobj);
				res.put("status", "000");
				res.put("msg", "success");
				
			} else if ("p".equals(action)) {
				String hql = "from Cuisine where users.uid =?";
				Object[] o = { jsonobj.get("uid") };
				List clist = CuisineServiceInter.executeQueryByPage(hql, o, (Integer) jsonobj.get("pageNow"),
						(Integer) jsonobj.get("pageSize"));
				JSONArray array = new JSONArray();
				JsonUtil.addCuisineList(array, clist);
				res.put("status", "000");
				res.put("msg", "success");
				res.put("pageCount", CuisineServiceInter.getPageCount("select count(*)" + hql, o,
						(Integer) jsonobj.get("pageSize")));
				res.put("cuisine", array);
			} else {
				res.put("status", "101");
				res.put("msg", "Invalid Parameters");
			}

		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();
			res.clear();
			res.put("status", "101");
			res.put("msg", "Invalid Parameters");
			throw(new  RuntimeException());
		}catch (JSONException e) {
			e.printStackTrace();
			res.clear();
			res.put("status", "101");
			res.put("msg", "Invalid Parameters");
			throw(new  RuntimeException());
		} 
		catch (Exception e) {
			e.printStackTrace();
			res.clear();
			res.put("status", "999");
			res.put("msg", e.getMessage());
			throw(new  RuntimeException());
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
