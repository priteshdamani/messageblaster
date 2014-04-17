package com.pir.wicket.web.components;

import com.pir.domain.User;
import com.pir.services.IBusinessService;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by pritesh on 12/10/13.
 */
public class BasePanel extends Panel{

    @SpringBean
    protected IBusinessService businessService;

    public BasePanel(String id) {
        super(id);
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
