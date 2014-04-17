package com.pir.wicket.web.pages;

import com.pir.exceptions.InvalidVerifyEmailCodeException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/9/13
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class VerifyEmailPage extends GeneralPage {

    public VerifyEmailPage(PageParameters pageParameters) {
        String code = pageParameters.get("code").toString();
        final String userId = pageParameters.get("id").toString();
        String title = "Success!";
        String message = "You are all set! Please login.";
        String buttonText = "Take me to my dashboard";
        try {
            if (code == null || code.trim().length() == 0) {
                title = "Resend Verification Email?";
                message = "Please click button below to resend the verification email.";
                final Label btnLbl = new Label("buttonLbl", "Re-send verification email");

                Link link = new Link("nextPage") {
                    public void onClick() {
                        businessService.resendVerificationEmail(Long.parseLong(userId));
                        this.remove(btnLbl);
                        Label newLbl = new Label("buttonLbl", "Please check your email. It could take up to 5 mins to get your email. Please check your spam folder if you dont see the email. ");
                        this.add(newLbl);
                        this.setEnabled(false);
                    }
                };
                link.add(btnLbl);
                add(link);
            } else {
                businessService.verifyEmail(Long.parseLong(userId), code);
                Link link = new Link("nextPage") {
                    public void onClick() {
                        throw new RedirectToUrlException("/Dashboard");
                    }
                };
                link.add(new Label("buttonLbl", buttonText));
                add(link);
            }
            add(new Label("title", title));
            add(new Label("msg", message));
        } catch (InvalidVerifyEmailCodeException e) {
            title = "Error!";
            message = "We were not able verify your email. Please request a verification email again.";
            add(new Label("title", title));
            add(new Label("msg", message));
            final Label btnLbl = new Label("buttonLbl", "Re-send verification email");

            Link link = new Link("nextPage") {
                public void onClick() {
                    businessService.resendVerificationEmail(Long.parseLong(userId));
                    this.remove(btnLbl);
                    Label newLbl = new Label("buttonLbl", "Please check your email. It could take up to 5 mins to get your email. Please check your spam folder if you dont see the email. ");
                    this.add(newLbl);
                    this.setEnabled(false);
                }
            };
            link.add(btnLbl);
            add(link);
        }
    }
}