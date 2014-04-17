package com.pir.wicket.web.pages;

import com.pir.domain.Group;
import com.pir.wicket.web.components.BaseForm;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupSettingsPage extends LoggedInBasePage {
    public GroupSettingsPage(PageParameters parameters) {
        super(parameters);
        Group group = businessService.groupGet(currentGroupId);
        GroupModel model = new GroupModel();
        model.setGroupName(group.getName());
        model.setGroupDescription(group.getDescription());
        add(new RegistrationForm("registrationForm", new CompoundPropertyModel<GroupModel>(model)));
    }


    private class RegistrationForm extends BaseForm {

        private RegistrationForm(String id, IModel<GroupModel> userModel) {
            super(id, userModel);
            add(new TextField("groupName"));
            add(new TextArea<String>("groupDescription"));
            add(new Button("saveButton"));
        }

        @Override
        public void onSubmit() {
            GroupModel info = (GroupModel) getDefaultModelObject();
            businessService.groupUpdate(currentGroupId,info.getGroupName(),info.getGroupDescription());
            getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("/Dashboard"));
        }
    }

    private class GroupModel implements Serializable {
        private String groupName;
        private String groupDescription;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupDescription() {
            return groupDescription;
        }

        public void setGroupDescription(String groupDescription) {
            this.groupDescription = groupDescription;
        }
    }
}
