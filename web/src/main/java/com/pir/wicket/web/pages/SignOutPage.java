package com.pir.wicket.web.pages;

import com.pir.wicket.web.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/9/13
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SignOutPage extends BasePage {

    public SignOutPage() {
        getSession().logout();
        HttpServletRequest request = (HttpServletRequest) getRequest().getContainerRequest();
        request.getSession().invalidate();
        CookieUtil.remove(CookieUtil.REMEMBER_ME);
        setResponsePage(LoginPage.class);
    }
}
