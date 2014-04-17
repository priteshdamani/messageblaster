package com.pir.wicket.web.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.flow.RedirectToUrlException;

/**
 * Created by pritesh on 12/12/13.
 */
public class LoggedInMenu extends Panel {
    public LoggedInMenu(String id, String loggedInName, final Long userId, boolean userVerified) {
        super(id);
        add(new Label("name",loggedInName));
        Link link = new Link("resendVerificationEmail") {
            public void onClick() {
                throw new RedirectToUrlException("/VerifyEmail?id="+userId.toString());
            }
        };
        link.add(new Label("resendLbl","Your email is not verified. Click here to resend verification email."));
        link.setVisible(!userVerified);
        add(link);
    }
}
