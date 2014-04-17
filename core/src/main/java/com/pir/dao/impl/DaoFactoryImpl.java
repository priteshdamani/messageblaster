package com.pir.dao.impl;

import com.pir.dao.DaoFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("daoFactory")
public class DaoFactoryImpl implements DaoFactory {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public <T> BaseDaoImpl<T> getDao(Class<T> type) {
		BaseDaoImpl<T> daoImpl = new BaseDaoImpl<T>(type);
		daoImpl.setSessionFactory(sessionFactory);
		return daoImpl;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	} 	
	
	
}
