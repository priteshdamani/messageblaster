package com.pir.func.services;

import com.pir.domain.Group;
import com.pir.domain.User;
import com.pir.domain.UserGroupLink;
import com.pir.exceptions.EmailAlreadyUsedException;
import com.pir.exceptions.UsernameAlreadyUsedException;
import com.pir.setup.BaseFuncTest;
import junit.framework.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Created by pritesh on 12/19/13.
 */
public class MessageFuncTest extends BaseFuncTest {

    @Test
    public void testUserGroupRegistrationAndJoin() throws EmailAlreadyUsedException, UsernameAlreadyUsedException {
        String prefix = RandomStringUtils.randomAlphanumeric(5);
        User user = new User(prefix, prefix, prefix + "@example.com", prefix, prefix);
        user = businessService.registerUserAndGroup(user, "Group Name", "Group Description");
        Assert.assertEquals(1, user.getGroups().size());
        user = businessService.userGet(user.getId());
        assertEquals(UserGroupLink.Role.ADMIN, user.getRole(user.getGroups().get(0).getId()));

        String prefix2 = RandomStringUtils.randomAlphanumeric(5);
        User user2 = new User(prefix2, prefix2, prefix2 + "@example.com", prefix2, prefix2);
        user2 = businessService.registerUserAndJoinGroup(user2, user.getGroups().get(0).getCode());
        businessService.joinGroup(user.getGroups().get(0).getCode(), user2.getId());
        Group group = businessService.groupGet(user.getGroups().get(0).getId());

        prefix2 = RandomStringUtils.randomAlphanumeric(5);
        user2 = new User(prefix2, prefix2, prefix2 + "@example.com", prefix2, prefix2);
        user2 = businessService.registerUserAndJoinGroup(user2, user.getGroups().get(0).getCode());
        businessService.joinGroup(user.getGroups().get(0).getCode(), user2.getId());
        group = businessService.groupGet(user.getGroups().get(0).getId());

        assertEquals(0, businessService.messagesGet(group.getId(), 50).size());

        businessService.sendMessage(user.getId(), group.getId(), "test", "test");

        assertEquals(1, businessService.messagesGet(group.getId(), 50).size());

    }

}
