package com.pir.wicket.web.pages;

import com.pir.domain.User;
import com.pir.wicket.web.components.BaseForm;
import com.pir.wicket.web.components.NotificationPanel;
import com.pir.wicket.web.util.CookieUtil;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginPage extends PlainPage {

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private String redirectToUrl = null;

    public LoginPage(PageParameters parameters) {
        this(parameters,null);
    }

    public LoginPage(PageParameters parameters, String redirectBacktoUrl){
        this((!parameters.get("groupCode").isEmpty() && !parameters.get("groupCode").isNull()) ? "/Group/"+parameters.get("groupCode") : null);
    }

    public LoginPage(String redirectToUrl) {

        Class redirectClass = DashboardPage.class;
        this.redirectToUrl = redirectToUrl;

        //Check for cookies from remember-me
        if (!getSession().isLoggedIn()) {
            String userId = CookieUtil.load(CookieUtil.REMEMBER_ME);
            if (userId != null) {
                try {
                    User user = businessService.userGet(Long.parseLong(userId));
                    if (user == null){
                        CookieUtil.remove(CookieUtil.REMEMBER_ME);
                    } else {
                        getSession().setUserId(Long.parseLong(userId));
                    }
                    setResponsePage(redirectClass);
                } catch (Exception e) {
                    // then don't worry, just delete the cookie and move on
                    CookieUtil.remove(CookieUtil.REMEMBER_ME);
                    logger.error("Problem with remember me stuff", e);
                }
            }
        } else {
            setResponsePage(redirectClass);
        }

        //Check if user is still not logged in; show login screen
        LoginForm form = new LoginForm("loginForm", new CompoundPropertyModel<LoginModel>(new LoginModel()), redirectClass, redirectToUrl);
        add(form);
    }



    private class LoginForm extends BaseForm {
        private Class redirect;
        private NotificationPanel feedbackPanel;
        private String redirectToUrl;

        private LoginForm(String id, IModel<LoginModel> userModel, Class redirect, String redirectToUrl) {
            super(id, userModel);
            this.redirect = redirect;
            this.redirectToUrl = redirectToUrl;
            feedbackPanel = new NotificationPanel("feedback");
            feedbackPanel.setOutputMarkupId(true);
            add(feedbackPanel);
            add(new TextField("identifier"));
            add(new PasswordTextField("password"));
            add(new Button("login"));
            add(new CheckBox("rememberMe"));
        }

        @Override
        public void onSubmit() {

            logger.debug("logging in user");
            LoginModel loginInfo = (LoginModel) getDefaultModelObject();
            String identifier = loginInfo.getIdentifier();

            Long authenticatedUserId = businessService.authenticate(identifier, loginInfo.getPassword());

            if(authenticatedUserId == null){
                logger.info("login failed for user {}", identifier);
                info("Invalid username/password combination");
            } else {
                logger.debug("login succeeded for user {}", identifier);
                getSession().setUserId(authenticatedUserId);
                CookieUtil.save(CookieUtil.UID, authenticatedUserId.toString());
                if(loginInfo.isRememberMe()){
                    CookieUtil.save(CookieUtil.REMEMBER_ME, authenticatedUserId.toString());
                }

                //System.out.println("Redirecting ...... ");
                if (redirectToUrl != null){
                    throw new RedirectToUrlException(redirectToUrl);
                } else {
                    setResponsePage(redirect);
                }
            }
        }
    }

    private class LoginModel implements Serializable {
        private String identifier;
        private String password;
        private boolean rememberMe;

        private String getIdentifier() {
            return identifier;
        }

        private void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        private String getPassword() {
            return password;
        }

        private void setPassword(String password) {
            this.password = password;
        }

        private boolean isRememberMe() {
            return rememberMe;
        }

        private void setRememberMe(boolean rememberMe) {
            this.rememberMe = rememberMe;
        }
    }

}
