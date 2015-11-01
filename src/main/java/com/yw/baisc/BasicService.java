package com.yw.baisc;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class BasicService implements BasicServiceInter {

	@Resource
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Object findById(Class clazz, Serializable id) {

//		return null;
		return sessionFactory.getCurrentSession().get(clazz, id);
	}

	public List executeQurey(String hql, Object[] parameters) {

		Query query = sessionFactory.getCurrentSession().createQuery(hql);

		if (parameters != null && parameters.length > 0) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i, parameters[i]);
			}
		}

		List list = query.list();
		if (list.size() > 0)
			return list;
		return null;
	}

	public List executeQueryByPage(String hql, Object[] parameters, int pageNow, int pageSize) {

		Query query = sessionFactory.getCurrentSession().createQuery(hql);

		if (parameters != null && parameters.length > 0) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i, parameters[i]);
			}
		}

		// Paging
		List list = query.setFirstResult((pageNow - 1) * pageSize).setMaxResults(pageSize).list();
		if (list.size() > 0)
			return list;
		return null;
	}

	public Serializable add(Object object) {

		return sessionFactory.getCurrentSession().save(object);

	}

	public void executeUpdate(Object object) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().update(object);
	}

	public void executeDelete(Object object) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().delete(object);
	}

	public Object uniqueQuery(String hql, Object[] parameters) {
		// TODO Auto-generated method stub
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql);

		if (parameters != null && parameters.length > 0) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i, parameters[i]);
			}
		}
		return query.uniqueResult();
	}

}
