package com.pir.dao.impl;

import com.pir.dao.IGroupDao;
import com.pir.domain.Group;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pritesh on 12/8/13.
 */

@Repository
public class GroupDao extends BaseDaoImpl<Group> implements IGroupDao {
    public GroupDao() {
        super(Group.class);
    }

    @Override
    public Group searchGroupByGroupCode(String groupCode) {
        List<Group> groups = findAllByCriteria(Restrictions.eq("code", groupCode));
        if (groups != null && groups.size() > 0){
            return groups.get(0);
        } else {
            return null;
        }
    }
}
