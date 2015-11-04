package com.yw.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.yw.domain.Users;
import com.yw.service.imp.UsersService;
import com.yw.service.inter.UsersServiceInter;
import com.yw.util.JsonDateValueProcessor;
import com.yw.util.JsonUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

public class UserAPI extends GenericServlet implements Servlet {

	private static final long serialVersionUID = 1L;

	private UsersServiceInter usersServiceInter;

	public UserAPI() {
		super();
	}

	public void init() throws ServletException {

		super.init();

	}
	
	public void sayHello(){
		System.out.println("Hello User");
	}

	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		String action = (String) req.getParameter("action");
		System.out.println(action);
		sayHello();
		String json = handle((String) req.getParameter("action"), (String) req.getParameter("json"));
		res.getWriter().println(json);

		/*
		 * System.out.println(action); res.getWriter().println(action); // to
		 * do: parse json string to get the users object Users user =
		 * (Users)this.usersServiceInter.findById(Users.class,
		 * Integer.parseInt("1"));
		 * 
		 * res.getWriter().println(user.getName());
		 * System.out.println("dealing......");
		 */
	}

	@SuppressWarnings("finally")
	public String handle(String action, String json) throws RuntimeException {
		JSONObject jsonobj = JSONObject.fromObject(json);
		JSONObject res = new JSONObject();
		try {
			if ("c".equals(action)) {
				Users user = (Users) JSONObject.toBean(jsonobj, Users.class);
				String uid = usersServiceInter.add(user).toString();
				res.put("status", "000");
				res.put("msg", "success");
				res.put("uid", uid);
			} else if ("u".equals(action)) {
				Users user = (Users) JSONObject.toBean(jsonobj, Users.class);
				usersServiceInter.executeUpdate(user);
				res.put("status", "000");
				res.put("msg", "success");
			} else if ("r".equals(action)) {
				Users user = (Users) JSONObject.toBean(jsonobj, Users.class);
				boolean flag = false;
				if (user.getUid() != null) {
					// check user info, in case needed
					user = (Users) usersServiceInter.findById(Users.class, user.getUid());
					flag = true;
				} else {
					user = usersServiceInter.logginCheck(user);
				}

				if (user != null) {
					res.put("status", "000");
					res.put("msg", "success");
					JsonUtil.addSimpleUser(res, user);
				} else if (flag) {
					res.put("status", "003");
					res.put("msg", "Not Found in Database");
				} else {
					res.put("status", "002");
					res.put("msg", "Invalid Username or Passowrd");
				}

			} else if ("d".equals(action)) {
				Users user = (Users) JSONObject.toBean(jsonobj, Users.class);
				if (user.getUid() == null) {
					res.put("status", "004");
					res.put("msg", "Incomplete Parameters");
				} else {
					usersServiceInter.executeDelete(user);
					res.put("status", "000");
					res.put("msg", "success");
				}
			} else {
				res.put("status", "001");
				res.put("msg", "Illegal Parameters");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", "999");
			res.put("msg", e.getMessage());

		} finally {

			return res.toString();
		}

	}

	public UsersServiceInter getUsersServiceInter() {
		return usersServiceInter;
	}

	public void setUsersServiceInter(UsersServiceInter usersServiceInter) {
		this.usersServiceInter = usersServiceInter;
	}

}
