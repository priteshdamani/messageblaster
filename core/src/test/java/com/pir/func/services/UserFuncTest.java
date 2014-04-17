package com.pir.func.services;

import com.pir.domain.User;
import com.pir.dto.ProfileDto;
import com.pir.exceptions.*;
import com.pir.setup.BaseFuncTest;
import junit.framework.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Created by pritesh on 12/19/13.
 */
public class UserFuncTest extends BaseFuncTest {

    @Test
    public void testUserRegistration() throws EmailAlreadyUsedException, UsernameAlreadyUsedException {

        long totalMessagesBefore = businessService.totalMessageRecordsGet();

        String prefix = RandomStringUtils.randomAlphanumeric(5);
        User user = new User(prefix, prefix, prefix + "@example.com", prefix, prefix);
        user = businessService.registerUser(user);

        long totalMessagesAfter = businessService.totalMessageRecordsGet();

        assertEquals(totalMessagesAfter, totalMessagesBefore + 1);


        try {
            businessService.registerUser(user);
            Assert.fail("Was able to register user again.");
        } catch (UsernameAlreadyUsedException e) {
            //expected
        } catch (EmailAlreadyUsedException e) {
            //expected
        }

        User user2 = new User(prefix, prefix, prefix + "11@example.com", prefix, prefix);
        try {
            businessService.registerUser(user2);
            Assert.fail("Was able to register user again.");
        } catch (UsernameAlreadyUsedException e) {
            //expected
        }

        user2 = new User(prefix + "11", prefix, prefix + "11@example.com", prefix, prefix);
        businessService.registerUser(user2);

        assertTrue(businessService.authenticate(user.getEmail(), prefix) != null);
        assertTrue(businessService.authenticate(user.getUsername(), prefix) != null);
        assertTrue(businessService.authenticate(user.getUsername(), prefix + "!!!") == null);

    }

    @Test
    public void testUserProfileUpdate() throws EmailAlreadyUsedException, UsernameAlreadyUsedException {
        String prefix = RandomStringUtils.randomAlphanumeric(5);
        User user = new User(prefix, prefix, prefix + "@example.com", prefix, prefix);
        user = businessService.registerUser(user);
        String pass1 = user.getPassword();

        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setCellCarrier(User.CellCarrier.ATT);
        user.setMobileNumber("1111111111");
        user.setEmail(prefix + "1@example.com");

        businessService.updateProfile(new ProfileDto(user));

        user = businessService.userGet(user.getId());
        assertEquals("First Name", user.getFirstName());
        assertEquals("Last Name", user.getLastName());
        assertEquals(User.CellCarrier.ATT, user.getCellCarrier());
        assertEquals("1111111111", user.getMobileNumber());
        assertEquals(prefix + "1@example.com", user.getEmail());
        assertEquals(pass1, user.getPassword());

        businessService.changePassword(user.getId(), "newpassword");

        user = businessService.userGet(user.getId());
        assertEquals("First Name", user.getFirstName());
        assertEquals("Last Name", user.getLastName());
        assertEquals(User.CellCarrier.ATT, user.getCellCarrier());
        assertEquals("1111111111", user.getMobileNumber());
        assertEquals(prefix + "1@example.com", user.getEmail());
        assertNotSame(pass1, user.getPassword());
    }

    @Test
    public void testForgotPasswordEtc() throws EmailAlreadyUsedException, UsernameAlreadyUsedException, EmailNotFoundException, UsernameNotFoundException, InvalidVerifyEmailCodeException {

        long totalMessagesBefore = businessService.totalMessageRecordsGet();

        String prefix = RandomStringUtils.randomAlphanumeric(5);
        User user = new User(prefix, prefix, prefix + "@example.com", prefix, prefix);
        user = businessService.registerUser(user);

        long totalMessagesAfter = businessService.totalMessageRecordsGet();
        assertEquals(totalMessagesAfter, totalMessagesBefore + 1);

        businessService.resendVerificationEmail(user.getId());
        totalMessagesAfter = businessService.totalMessageRecordsGet();
        assertEquals(totalMessagesAfter, totalMessagesBefore + 2);

        businessService.sendPasswordReset(user.getUsername(), null);
        totalMessagesAfter = businessService.totalMessageRecordsGet();
        assertEquals(totalMessagesAfter, totalMessagesBefore + 3);

        businessService.sendPasswordReset(null, user.getEmail());
        totalMessagesAfter = businessService.totalMessageRecordsGet();
        assertEquals(totalMessagesAfter, totalMessagesBefore + 4);

        try {
            businessService.sendPasswordReset(null, "test");
            Assert.fail("EmailNotFound Expected");
        } catch (EmailNotFoundException e) {
            //expected
        }

        try {
            businessService.sendPasswordReset("jumbalaya", null);
            Assert.fail("UsernameNotFoundException Expected");
        } catch (UsernameNotFoundException e) {
            //expected
        }

        assertFalse(user.isVerified());

        businessService.verifyEmail(user.getId(), user.getVerificationRecord());
        user = businessService.userGet(user.getId());

        assertTrue(user.isVerified());

        try {
            businessService.verifyEmail(user.getId(), "test");
            Assert.fail("Invalid Code Exception expected");
        } catch (InvalidVerifyEmailCodeException e) {
            //expected

        }


    }

}
