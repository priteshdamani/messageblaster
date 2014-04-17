package com.pir.dao;

import com.pir.domain.Group;

/**
 * Created by pritesh on 12/8/13.
 */
public interface IGroupDao extends IBaseDao<Group> {
    Group searchGroupByGroupCode(String groupCode);
}
