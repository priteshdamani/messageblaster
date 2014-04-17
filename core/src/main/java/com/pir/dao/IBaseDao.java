package com.pir.dao;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import java.util.Collection;
import java.util.List;

public interface IBaseDao<T>
{
	T save(T entity);
	
	Collection<T> saveAll(Collection<T> entities);

    T findById(Long id);

    List<T> findAll();
    
    List<T> findAllDistinctWithOrder(Order order);
    
    List<T> findAllWithOrder(Order order);
    
    T findOneByCriteria(Criterion... criteria);
    
    List<T> findAllByCriteria(Criterion... criteria);
    
    List<T> findAllByCriteriaDistinct(Criterion... criteria);
    
    List<T> findAllByCriteriaWithOrder(Order order, Criterion... criteria);

    List<T> findAllByCriteriaWithOrder(int pageSize, Order order, Criterion... criteria );

    List<T> findAllByCriteriaDistinctWithOrder(Order order, Criterion... criteria);
    
    void deleteAll(Collection<T> entities);
    
    /**
     * Deletes all rows from the Object Table
     * @return
     */
    int truncateTable();

    long count();
    
    void delete(T entity);
}
