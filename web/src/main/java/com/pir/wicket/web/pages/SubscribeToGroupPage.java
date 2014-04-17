package com.pir.wicket.web.pages;

import com.pir.domain.User;
import com.pir.exceptions.EmailAlreadyUsedException;
import com.pir.exceptions.UsernameAlreadyUsedException;
import com.pir.wicket.web.components.BaseForm;
import com.pir.wicket.web.components.NotificationPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/6/13
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscribeToGroupPage extends PlainPage {

    private final Logger logger = LoggerFactory.getLogger(CreateNewGroupPage.class);

    public SubscribeToGroupPage(PageParameters parameters) {
        if (isUserLoggedIn()){
            getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("/Group/"+parameters.get("groupCode").toString()));
        } else {
            RegistrationForm form = new RegistrationForm("subscribeForm", new CompoundPropertyModel<RegisterModel>(new RegisterModel(parameters)), "/Dashboard",parameters);
            add(form);
        }
    }


    private class RegistrationForm extends BaseForm {
        private String redirectUrl;
        private NotificationPanel feedbackPanel;

        private RegistrationForm(String id, IModel<RegisterModel> userModel, String redirectUrl,PageParameters parameters) {
            super(id, userModel);
            this.redirectUrl = redirectUrl;
            feedbackPanel = new NotificationPanel("feedback");
            feedbackPanel.setOutputMarkupId(true);
            add(feedbackPanel);
            add(new ExternalLink("loginLink","/Login?groupCode="+parameters.get("groupCode").toString(null)));
            add(new TextField("username"));
            add(new TextField("email"));
            add(new TextField("firstName"));
            add(new TextField("lastName"));
            add(new TextField("groupCode"));
            add(new TextField("mobileNumber"));
            add(new DropDownChoice<String>("mobileCarrier", User.CellCarrierTypesGet()));
            add(new PasswordTextField("password"));
            add(new PasswordTextField("passwordConfirmation"));
            add(new Button("subscribeButton"));
        }

        @Override
        public void onSubmit() {
            logger.debug("registering new user");
            RegisterModel info = (RegisterModel) getDefaultModelObject();
            User user = new User(info.getUsername(), info.getPassword(), info.getEmail(), info.getFirstName(), info.getLastName());
            if (info.getMobileNumber() != null){
                user.setMobileNumber(info.getMobileNumber());
                if (info.getMobileCarrier() == null){
                    error("Mobile Carrier has to be selected if you pick to enter your mobile number");
                    return;
                }
                user.setCellCarrier(User.CellCarrier.valueOf(info.getMobileCarrier()));
            }
            Long userId = null;
            try {
                user = businessService.registerUserAndJoinGroup(user, info.getGroupCode());
                userId = user.getId();
                getSession().setUserId(userId);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(redirectUrl));
            } catch (EmailAlreadyUsedException e) {
                logger.info("Email already used, please sign up with new email");
                error("Email already used, please sign up with new email");
                return;
            } catch (UsernameAlreadyUsedException e) {
                logger.info("Username already used");
                error("Username already used");
                return;
            }
        }
    }

    private class RegisterModel implements Serializable {
        private String username;
        private String email;
        private String password;
        private String passwordConfirmation;
        private String firstName;
        private String lastName;
        private String groupCode;
        private String mobileNumber;
        private String mobileCarrier;

        public RegisterModel(PageParameters parameters) {
            groupCode = parameters.get("groupCode").toString(null);
        }

        private String getFirstName() {
            return firstName;
        }

        private void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        private String getLastName() {
            return lastName;
        }

        private void setLastName(String lastName) {
            this.lastName = lastName;
        }

        private String getUsername() {
            return username;
        }

        private void setUsername(String username) {
            this.username = username;
        }

        private String getEmail() {
            return email;
        }

        private void setEmail(String email) {
            this.email = email;
        }

        private String getPassword() {
            return password;
        }

        private void setPassword(String password) {
            this.password = password;
        }

        private String getPasswordConfirmation() {
            return passwordConfirmation;
        }

        private void setPasswordConfirmation(String passwordConfirmation) {
            this.passwordConfirmation = passwordConfirmation;
        }

        private String getGroupCode() {
            return groupCode;
        }

        private void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getMobileCarrier() {
            return mobileCarrier;
        }

        public void setMobileCarrier(String mobileCarrier) {
            this.mobileCarrier = mobileCarrier;
        }
    }
}

