package com.yw.service.imp;

import com.yw.baisc.BasicService;
import com.yw.domain.Users;
import com.yw.service.inter.UsersServiceInter;

public class UsersService extends BasicService implements UsersServiceInter{

	public Users logginCheck(Users user) {
		// TODO Auto-generated method stub
		String hql="from Users user where user.name=? and user.password=?";
		Object[] parameters={user.getName(),user.getPassword()};
		
		return (Users)this.uniqueQuery(hql, parameters);
	}


}
