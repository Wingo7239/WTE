package com.yw.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.yw.domain.Users;
import com.yw.util.JsonUtil;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Users u = JsonUtil.parseUser("");
		System.out.println(u.getUid().getClass());
		System.out.println(JsonUtil.parseUser(""));
	}

}
