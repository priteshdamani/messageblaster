package com.pir.wicket.web.pages;

import com.pir.domain.User;
import com.pir.dto.ProfileDto;
import com.pir.exceptions.EmailAlreadyUsedException;
import com.pir.wicket.web.components.BaseForm;
import com.pir.wicket.web.components.NotificationPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfilePage extends LoggedInBasePage {
    public ProfilePage(PageParameters parameters) {
        super(parameters);
        ProfileForm form = new ProfileForm("profileForm", new CompoundPropertyModel<ProfileDto>(new ProfileDto(getLoggedInUser())));
        add(form);
        PasswordChangeForm passwordChangeForm = new PasswordChangeForm("passwordChangeForm", new CompoundPropertyModel<PasswordChangeDto>(new PasswordChangeDto(getLoggedInUserId())));
        add(passwordChangeForm);
    }



    private class PasswordChangeForm extends BaseForm{


        public PasswordChangeForm(String id,  IModel<PasswordChangeDto> iModel) {
            super(id, iModel);
            add(new HiddenField<Long>("userId"));
            add(new PasswordTextField("password"));
            add(new PasswordTextField("passwordConfirmation"));
        }

        @Override
        public void onSubmit() {
            PasswordChangeDto info = (PasswordChangeDto) getDefaultModelObject();
            businessService.changePassword(info.getUserId(), info.getPassword());
            getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("/Profile"));
        }
    }


    private class ProfileForm extends BaseForm{

        private final NotificationPanel feedbackPanel;

        public ProfileForm(String id,  IModel<ProfileDto> iModel) {
            super(id, iModel);
            feedbackPanel = new NotificationPanel("feedback");
            feedbackPanel.setOutputMarkupId(true);
            add(feedbackPanel);
            add(new HiddenField<Long>("userId"));
            add(new Label("username"));
            add(new TextField("email"));
            add(new TextField("firstName"));
            add(new TextField("lastName"));
            add(new TextField("mobileNumber"));
            add(new DropDownChoice<String>("mobileCarrier", User.CellCarrierTypesGet()));
            add(new Button("saveButton"));
        }
        @Override
        public void onSubmit() {
            ProfileDto info = (ProfileDto) getDefaultModelObject();
            if (info.getMobileNumber() != null){
                if (info.getMobileCarrier() == null){
                    error("Mobile Carrier has to be selected if you pick to enter your mobile number");
                    return;
                }
            }
            try {
                businessService.updateProfile(info);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("/Profile"));
            } catch (EmailAlreadyUsedException e) {
                error(e.getMessage());
            }
        }
    }

    private class PasswordChangeDto implements Serializable{
        private Long userId;
        private String password;
        private String passwordConfirmation;

        public PasswordChangeDto(Long loggedInUserId) {
            this.userId = loggedInUserId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPasswordConfirmation() {
            return passwordConfirmation;
        }

        public void setPasswordConfirmation(String passwordConfirmation) {
            this.passwordConfirmation = passwordConfirmation;
        }
    }




}
