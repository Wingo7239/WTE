package com.yw.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.NullArgumentException;
import org.springframework.transaction.annotation.Transactional;

import com.yw.domain.Users;
import com.yw.service.imp.UsersService;
import com.yw.service.inter.UsersServiceInter;
import com.yw.util.JsonDateValueProcessor;
import com.yw.util.JsonUtil;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONException;
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

	public void sayHello() {
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
	@Transactional
	public String handle(String action, String json) throws RuntimeException {

		JSONObject res = new JSONObject();
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd" }));

		try {
			JSONObject jsonobj = JSONObject.fromObject(json);

			if (jsonobj == null) {
				res.put("status", "001");
				res.put("msg", "Illegal Parameters");
				return res.toString();
			}
			if ("c".equals(action)) {
				Users user = (Users) JSONObject.toBean(jsonobj, Users.class);
				// require name & password
				if (user.getName() == null || user.getPassword() == null) {
					res.put("status", "101");
					res.put("msg", "Invalid Parameters");
				} else {
					String uid = usersServiceInter.add(user).toString();
					res.put("status", "000");
					res.put("msg", "success");
					res.put("uid", uid);
				}
			} else if ("u".equals(action)) {
				Users user = (Users) JSONObject.toBean(jsonobj, Users.class);
				// require uid
				if (user.getUid() == null || user.getUid() == 0 || user.getName() == null || user.getPassword() == null) {
					res.put("status", "101");
					res.put("msg", "Invalid Parameters");
				} else {
					usersServiceInter.executeUpdate(user);
					res.put("status", "000");
					res.put("msg", "success");
				}
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
					res.put("status", "103");
					res.put("msg", "Not Found");
				} else {
					res.put("status", "102");
					res.put("msg", "Invalid Username or Passowrd");
				}

			} else if ("d".equals(action)) {
				Users user = (Users) JSONObject.toBean(jsonobj, Users.class);
				if (user.getUid() == null || user.getUid() == 0) {
					res.put("status", "101");
					res.put("msg", "Invalid Parameters");
				} else {
					usersServiceInter.executeDelete(user);
					res.put("status", "000");
					res.put("msg", "success");
				}
			} else {
				res.put("status", "101");
				res.put("msg", "Illegal Parameters");
			}
		} catch (NullPointerException e1) {
			res.put("status", "101");
			res.put("msg", "Illegal Parameters");
		} catch (JSONException e2) {
			res.put("status", "101");
			res.put("msg", "Illegal Parameters");

		}

		catch (Exception e) {
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
