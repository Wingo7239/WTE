package com.yw.test;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.yw.domain.Contains;
import com.yw.domain.Cuisine;
import com.yw.domain.Ingredients;
import com.yw.domain.Users;
import com.yw.service.imp.UsersService;
import com.yw.service.inter.UsersServiceInter;
import com.yw.util.JsonUtil;

import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
////		System.out.println(JsonUtil.test());
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		TestService ts  = (TestService) ac.getBean("test");
		ts.sayHello();
//		
	
		
	}

}
