package com.yw.baisc;

import java.io.Serializable;
import java.util.List;

import com.yw.domain.Cuisine;

public interface BasicServiceInter {

	// find by ID
	public Object findById(Class clazz, java.io.Serializable id);
	// query
	public List executeQurey(String hql, Object[] parameters);
	//query by page
	public List executeQueryByPage(String hql, Object[] parameters,int pageNow, int pageSize);
	//add
	public Serializable add(Object object);
	//update
	public void executeUpdate(Object object);
	//delete
	public void executeDelete(Object object);
	//Single result query
	public Object uniqueQuery(String hql, Object[] parameters);
	//getPageCount
	public int getPageCount(String hql, Object[] parameters,int pageSize);
}
