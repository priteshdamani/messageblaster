package com.pir.wicket.web.pages;

import com.pir.exceptions.EmailNotFoundException;
import com.pir.exceptions.UsernameNotFoundException;
import com.pir.wicket.web.components.BaseForm;
import com.pir.wicket.web.components.NotificationPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForgotPasswordPage extends PlainPage {

    public ForgotPasswordPage() {
        NotificationPanel feedbackPanel = new NotificationPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        add(new UsernameReset("usernameResetForm",new CompoundPropertyModel<UsernameModel>(new UsernameModel())));

        add(new EmailReset("emailResetForm",new CompoundPropertyModel<EmailModel>(new EmailModel())));

    }

    private class UsernameReset extends BaseForm {

        private UsernameReset(String id, IModel<UsernameModel> userModel) {
            super(id, userModel);
            add(new TextField("username"));
        }

        @Override
        public void onSubmit() {
            UsernameModel info = (UsernameModel) getDefaultModelObject();
            try {
                businessService.sendPasswordReset(info.getUsername(), null);
                error("Please check your email for a reset link");
            } catch (EmailNotFoundException e) {
                error(e.getMessage());
            } catch (UsernameNotFoundException e) {
                error(e.getMessage());
            }
        }
    }

    private class EmailReset extends BaseForm {

        private EmailReset(String id, IModel<EmailModel> userModel) {
            super(id, userModel);
            add(new TextField("email"));
        }

        @Override
        public void onSubmit() {
            EmailModel info = (EmailModel) getDefaultModelObject();
            try {
                businessService.sendPasswordReset(null, info.getEmail());
                error("Please check your email for a reset link");
            } catch (EmailNotFoundException e) {
                error(e.getMessage());
            } catch (UsernameNotFoundException e) {
                error(e.getMessage());
            }
        }
    }

    private class UsernameModel implements Serializable{
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    private class EmailModel implements Serializable{
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
