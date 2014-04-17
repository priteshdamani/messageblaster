package com.pir.dao.impl;

import com.pir.dao.IUserDao;
import com.pir.domain.User;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pritesh on 12/8/13.
 */

@Repository
public class UserDao extends BaseDaoImpl<User> implements IUserDao {
    public UserDao() {
        super(User.class);
    }

    @Override
    public User searchUserByUsername(String username) {
        List<User> users = findAllByCriteria(Restrictions.eq("username", username));
        if (users != null && users.size() > 0){
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User SearchUserByEmail(String email) {
        List<User> users = findAllByCriteria(Restrictions.eq("email", email));
        if (users != null && users.size() > 0){
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<User> searchUsersForGroup(Long groupId) {
        Query query = getSession().createQuery("select distinct u from User u left join u.groupLinks ug where ug.group.id = :groupId ");
        query.setParameter("groupId",groupId);
        return query.list();
    }
}
