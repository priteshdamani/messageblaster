package com.pir.dao;

import com.pir.dao.impl.BaseDaoImpl;

public interface DaoFactory {

    <T> BaseDaoImpl<T> getDao(Class<T> type);

}
