package com.pir.wicket.web.pages;

import com.pir.domain.Group;
import com.pir.domain.User;
import com.pir.wicket.web.components.BaseForm;
import com.pir.wicket.web.components.NotificationPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextArea;
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
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddGroup extends PlainPage {

    private final Logger logger = LoggerFactory.getLogger(CreateNewGroupPage.class);

    public AddGroup() {
        RegistrationForm form = new RegistrationForm("registerForm", new CompoundPropertyModel<RegisterModel>(new RegisterModel()), "/Dashboard");
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
            add(new TextField("groupName"));
            add(new TextArea<String>("groupDescription"));
            add(new Button("register"));
        }

        @Override
        public void onSubmit() {
            logger.debug("registering new user");
            RegisterModel info = (RegisterModel) getDefaultModelObject();
            User user = getLoggedInUser();
            Group group = businessService.registerGroup(info.getGroupName(), info.getGroupDescription(), user.getId());
            getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("/GenericMessage?title=Group Created. Your group number is "+group.getCode()+"&msg=You can now send this code out to people you wish to send messages to! You will find this code on the next page as well!"));
        }
    }

    private class RegisterModel implements Serializable {
        private String groupName;
        private String groupDescription;


        private String getGroupName() {
            return groupName;
        }

        private void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        private String getGroupDescription() {
            return groupDescription;
        }

        private void setGroupDescription(String groupDescription) {
            this.groupDescription = groupDescription;
        }
    }
}