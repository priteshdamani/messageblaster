package com.pir.wicket.web.pages;

import com.pir.domain.Group;
import com.pir.wicket.web.components.BaseForm;
import com.pir.wicket.web.components.NotificationPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/9/13
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoinGroupPage extends PlainPage {
    private final Logger logger = LoggerFactory.getLogger(JoinGroupPage.class);

    public JoinGroupPage() {
        RegistrationForm form = new RegistrationForm("joinForm", new CompoundPropertyModel<RegisterModel>(new RegisterModel()), "/Group");
        add(form);
    }


    private class RegistrationForm extends BaseForm {
        private String redirectUrl;
        private NotificationPanel feedbackPanel;

        private RegistrationForm(String id, IModel<RegisterModel> userModel, String redirectUrl) {
            super(id, userModel);
            this.redirectUrl = redirectUrl;
            feedbackPanel = new NotificationPanel("feedback");
            feedbackPanel.setOutputMarkupId(true);
            add(feedbackPanel);
            add(new TextField("groupCode"));
            add(new Button("joinButton"));
        }

        @Override
        public void onSubmit() {
            RegisterModel info = (RegisterModel) getDefaultModelObject();
            Group group = businessService.searchGroup(info.getGroupCode().trim());
            if (group == null){
                error("Code error, no group with this code found. Are you sure you have the correct code?");
            } else {
                getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(redirectUrl+"/"+group.getCode()));
            }
        }
    }

    private class RegisterModel implements Serializable {
        private String groupCode;

        private String getGroupCode() {
            return groupCode;
        }

        private void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }
    }



}
