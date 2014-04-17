package com.pir.func.services;

import com.pir.domain.Group;
import com.pir.domain.User;
import com.pir.exceptions.EmailAlreadyUsedException;
import com.pir.exceptions.UsernameAlreadyUsedException;
import com.pir.setup.BaseFuncTest;
import junit.framework.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Created by pritesh on 12/19/13.
 */
public class GroupFuncTest extends BaseFuncTest {

    @Test
    public void testGroupUpdate() throws EmailAlreadyUsedException, UsernameAlreadyUsedException {
        String prefix = RandomStringUtils.randomAlphanumeric(5);
        User user = new User(prefix, prefix, prefix + "@example.com", prefix, prefix);
        user = businessService.registerUserAndGroup(user, "Group Name", "Group Description");
        Assert.assertEquals(1, user.getGroups().size());
        Group group = businessService.groupGet(user.getGroups().get(0).getId());
        businessService.groupUpdate(group.getId(), "Test Name", "Test Description");
        group = businessService.groupGet(group.getId());
        Assert.assertEquals("Test Name", group.getName());
        Assert.assertEquals("Test Description", group.getDescription());
    }

}
