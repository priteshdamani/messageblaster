package com.pir.dao.impl;

import com.pir.dao.IBaseDao;
import com.pir.util.MessageBlasterConstants;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings( "unchecked" )
public class BaseDaoImpl<T> implements IBaseDao<T>
{
	private static final Logger log = LoggerFactory.getLogger(BaseDaoImpl.class);
	
	private Class<T> persistentClass;
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public void setSessionFactory( SessionFactory sessionFactory )
	{
		this.sessionFactory = sessionFactory;
	}
	
	public BaseDaoImpl(Class<T> persistentClass )
	{
		this.persistentClass =  persistentClass;
		
	}

	protected Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}

	protected Class<T> getPersistentClass()
	{
		return persistentClass;
	}
	
	protected Criteria createCriteria( Criterion... criteria )
	{
		Criteria c = getSession().createCriteria( getPersistentClass() );
		
		for( Criterion criterion : criteria )
		{
			c.add( criterion );
		}
		
		return c;
	}
	
	public T save( T entity )
	{
		getSession().saveOrUpdate( entity );
		return entity;
	}
	
	public Collection<T> saveAll( Collection<T> entities ) {
		Collection<T> results = new ArrayList<T>();
		for(T eachEntity : entities) {
			T result = this.save(eachEntity);
			if(result!=null) {
				results.add(result);
			}
		}
		return entities;
	}
	
	public void delete(T entity) {
		getSession().delete(entity);
	}
	
	public void deleteAll(Collection <T> entities) {
		for( T eachEntity : entities) {
			this.delete(eachEntity);
		}
	}
	
	public int truncateTable() {
	    String hql = String.format("delete from %s", getPersistentClass().getSimpleName());
	    Query query = getSession().createQuery(hql);
	    return query.executeUpdate();
	}

	public T findById( Long id )
	{
		return (T) getSession().get( getPersistentClass(), id );
	}

	public List<T> findAll()
	{
		return createCriteria().list();
	}
	
	public List<T> findAllWithOrder( Order order )
	{
		return createCriteria().addOrder( order ).list();
	}

	public T findOneByCriteria( Criterion... criteria )
	{
		return (T) createCriteria( criteria ).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
	}

	public T findOneByCriteriaAndOrder( Order order , Criterion... criteria)
	{
		return (T) createCriteria( criteria ).addOrder(order).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
	}
	public List<T> findAllByCriteria( Criterion... criteria )
	{
		return createCriteria( criteria ).list();
	}		
	
	public List<T> findAllByCriteriaDistinct( Criterion... criteria )
	{
		return createCriteria( criteria ).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	public List<T> findAllByCriteriaWithOrder( Order order, Criterion... criteria )
	{
		return createCriteria( criteria ).addOrder( order ).list();
	}

    public List<T> findAllByCriteriaWithOrder(int pageSize, Order order, Criterion... criteria )
	{
		Criteria criterion = createCriteria(criteria);
        if (pageSize != MessageBlasterConstants.UNSET_PAGE_SIZE) {
            criterion.setMaxResults(pageSize);
            criterion.setFirstResult(0);
        }
        return criterion.addOrder(order).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	public List<T> findAllByCriteriaDistinctWithOrder( Order order, Criterion... criteria )
	{
		return createCriteria( criteria ).addOrder( order ).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	public long count()
	{
		return ( ( Long ) createCriteria().setProjection( Projections.rowCount() ).uniqueResult() ).longValue();
	}

	@Override
	public List<T> findAllDistinctWithOrder(Order order) {
		return createCriteria().addOrder(order).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
}
