package com.pir.wicket.web.pages;

import com.pir.domain.User;
import com.pir.services.IBusinessService;
import com.pir.wicket.web.components.CustomSession;
import com.pir.wicket.web.components.TrackingPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasePage extends WebPage {


    @SpringBean
    protected IBusinessService businessService;

    public BasePage() {
        add(new TrackingPanel("trackingPnl"));
    }

    @Override
    public CustomSession getSession() {
        return (CustomSession) super.getSession();
    }

    public Long getLoggedInUserId() {
        return getSession().getUserId();
    }

    public User getLoggedInUser() {
        long userId = getSession().getUserId();
        return businessService.userGet(userId);
    }


    public boolean isUserLoggedIn() {
        Long userId = getSession().getUserId();
        return userId != null;
    }
}
