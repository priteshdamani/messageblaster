package com.pir.dao;

import com.pir.domain.User;

import java.util.List;

/**
 * Created by pritesh on 12/8/13.
 */
public interface IUserDao extends IBaseDao<User> {
    User searchUserByUsername(String username);

    User SearchUserByEmail(String email);

    List<User> searchUsersForGroup(Long groupId);
}
