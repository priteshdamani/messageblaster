package com.pir.func.services;

import com.pir.domain.Group;
import com.pir.domain.User;
import com.pir.domain.UserGroupLink;
import com.pir.exceptions.EmailAlreadyUsedException;
import com.pir.exceptions.NotAnAdminException;
import com.pir.exceptions.UsernameAlreadyUsedException;
import com.pir.setup.BaseFuncTest;
import junit.framework.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Created by pritesh on 12/19/13.
 */
public class UserGroupFuncTest extends BaseFuncTest {

    @Test
    public void testUserGroupRegistration() throws EmailAlreadyUsedException, UsernameAlreadyUsedException {
        String prefix = RandomStringUtils.randomAlphanumeric(5);
        User user = new User(prefix, prefix, prefix + "@example.com", prefix, prefix);
        user = businessService.registerUserAndGroup(user, "Group Name", "Group Description");
        Assert.assertEquals(1, user.getGroups().size());
        user = businessService.userGet(user.getId());
        Assert.assertEquals(1, user.getGroups().get(0).getUserLinks().size());
    }

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
        Assert.assertEquals(1, user2.getGroups().size());
        assertEquals(UserGroupLink.Role.CAN_RECEIVE_MESSAGES, user2.getRole(user2.getGroups().get(0).getId()));

        businessService.joinGroup(user.getGroups().get(0).getCode(), user2.getId());
        Group group = businessService.groupGet(user.getGroups().get(0).getId());
        Assert.assertEquals(2, group.getUsers().size());

        Assert.assertNotNull(businessService.searchGroup(user.getGroups().get(0).getCode()));

        Assert.assertEquals(2, businessService.groupMembersGet(user.getGroups().get(0).getId()).size());

    }

    @Test
    public void testGroupMembership() throws EmailAlreadyUsedException, UsernameAlreadyUsedException, NotAnAdminException {
        String prefix = RandomStringUtils.randomAlphanumeric(5);
        User user = new User(prefix, prefix, prefix + "@example.com", prefix, prefix);
        user = businessService.registerUserAndGroup(user, "Group Name", "Group Description");
        Assert.assertEquals(1, user.getGroups().size());
        user = businessService.userGet(user.getId());
        assertEquals(UserGroupLink.Role.ADMIN, user.getRole(user.getGroups().get(0).getId()));

        String prefix2 = RandomStringUtils.randomAlphanumeric(5);
        User user2 = new User(prefix2, prefix2, prefix2 + "@example.com", prefix2, prefix2);
        user2 = businessService.registerUserAndJoinGroup(user2, user.getGroups().get(0).getCode());
        Assert.assertEquals(1, user2.getGroups().size());
        assertEquals(UserGroupLink.Role.CAN_RECEIVE_MESSAGES, user2.getRole(user2.getGroups().get(0).getId()));

        businessService.joinGroup(user.getGroups().get(0).getCode(), user2.getId());
        Group group = businessService.groupGet(user.getGroups().get(0).getId());
        Assert.assertEquals(2, group.getUsers().size());

        Assert.assertNotNull(businessService.searchGroup(user.getGroups().get(0).getCode()));

        Assert.assertEquals(2, businessService.groupMembersGet(user.getGroups().get(0).getId()).size());
        try {
            businessService.privilegeChange(user2.getId(), user2.getId(), group.getId(), UserGroupLink.Role.CAN_SEND_MESSAGES);
            Assert.fail("Not an admin expected");
        } catch (NotAnAdminException e) {
            //expected
        }
        businessService.privilegeChange(user.getId(), user2.getId(), group.getId(), UserGroupLink.Role.CAN_SEND_MESSAGES);
        user2 = businessService.userGet(user2.getId());
        assertEquals(UserGroupLink.Role.CAN_SEND_MESSAGES, user2.getRole(user2.getGroups().get(0).getId()));

        businessService.notificationOptionsUpdate(user2.getId(), group.getId(), false, false);

        user2 = businessService.userGet(user2.getId());

        assertFalse(user2.isReceivingEmail(group.getId()));
        assertFalse(user2.isReceivingTxt(group.getId()));

    }


    @Test
    public void testUserGroupMembershipDeletion() throws EmailAlreadyUsedException, UsernameAlreadyUsedException, NotAnAdminException {
        String prefix = RandomStringUtils.randomAlphanumeric(5);
        User user = new User(prefix, prefix, prefix + "@example.com", prefix, prefix);
        user = businessService.registerUserAndGroup(user, "Group Name", "Group Description");
        user = businessService.userGet(user.getId());

        String prefix2 = RandomStringUtils.randomAlphanumeric(5);
        User user2 = new User(prefix2, prefix2, prefix2 + "@example.com", prefix2, prefix2);
        user2 = businessService.registerUserAndJoinGroup(user2, user.getGroups().get(0).getCode());
        businessService.joinGroup(user.getGroups().get(0).getCode(), user2.getId());

        String prefix3 = RandomStringUtils.randomAlphanumeric(5);
        User user3 = new User(prefix3, prefix3, prefix3 + "@example.com", prefix3, prefix3);
        user3 = businessService.registerUserAndJoinGroup(user3, user.getGroups().get(0).getCode());
        businessService.joinGroup(user.getGroups().get(0).getCode(), user3.getId());

        Group group = businessService.groupGet(user.getGroups().get(0).getId());

        try {
            businessService.deleteMembership(user3.getId(), user2.getId(), group.getId());
            Assert.fail("NotAnAdminException expected");
        } catch (NotAnAdminException e) {
            //expected
        }

        int before = businessService.groupMembersGet(group.getId()).size();

        businessService.deleteMembership(user.getId(), user2.getId(), group.getId());

        int after = businessService.groupMembersGet(group.getId()).size();

        Assert.assertTrue(after == before - 1);


    }

}
