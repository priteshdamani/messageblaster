package com.pir.hibernate;

import com.pir.domain.AbstractChildObject;
import com.pir.domain.AbstractPrimaryObject;
import com.pir.domain.DomainObject;
import com.pir.util.AppClock;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;

/**
 * Created by pritesh on 12/10/13.
 */

public class EntityInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity,Serializable id, Object[] state, String[] propertyNames, Type[] types){
        if(entity instanceof DomainObject) {
            for(int i = 0; i < propertyNames.length ; i++){
                if("createdDate".equals(propertyNames[i])){
                    state[i] = AppClock.now();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {

        if(entity instanceof AbstractPrimaryObject || entity instanceof AbstractChildObject) {
            for(int i = 0; i < propertyNames.length ; i++){
                if("modifiedDate".equals(propertyNames[i])){
                    currentState[i] = AppClock.now();
                    return true;
                }
            }
        }
        return false;
    }

}

