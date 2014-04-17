package com.pir.wicket.web.pages;

import com.pir.wicket.web.components.BaseForm;
import com.pir.wicket.web.components.NotificationPanel;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;

/**
 * Created by pritesh on 12/16/13.
 */
public class ChangePasswordPage extends PlainPage {

    public ChangePasswordPage(PageParameters pageParameters) {
        final String userId = pageParameters.get("id").toString();
        NotificationPanel feedbackPanel = new NotificationPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
        add(new ChangePasswordForm("passwordChangeForm",new CompoundPropertyModel<PasswordModel>(new PasswordModel(userId))));

    }

    private class ChangePasswordForm extends BaseForm {

        private ChangePasswordForm(String id, IModel<PasswordModel> userModel) {
            super(id, userModel);
            add(new PasswordTextField("password"));
            add(new PasswordTextField("confirmPassword"));
        }

        @Override
        public void onSubmit() {
            PasswordModel info = (PasswordModel) getDefaultModelObject();
            businessService.changePassword(info.getUserId(), info.getPassword());
            getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("/Dashboard"));
        }
    }


    private class PasswordModel implements Serializable {
        private final long userId;
        private String password;
        private String confirmPassword;

        public PasswordModel(String userId) {
            this.userId = Long.parseLong(userId);
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public long getUserId() {
            return userId;
        }
    }

}
