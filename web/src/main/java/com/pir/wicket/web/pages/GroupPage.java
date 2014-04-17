package com.pir.wicket.web.pages;

import com.pir.domain.Group;
import com.pir.wicket.web.components.BaseForm;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/9/13
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupPage extends PlainPage {

    public GroupPage(PageParameters parameters) {
        StringValue groupCode = parameters.get(0);
        if (groupCode.toString("null").equals("null")){
            throw new RedirectToUrlException("/JoinGroup");
        } else {
            Group group = businessService.searchGroup(groupCode.toString());
            RegistrationForm form = new RegistrationForm("joinForm", group);
            add(form);
        }
    }


    private class RegistrationForm extends BaseForm {

        private RegistrationForm(String id, Group group) {
            super(id);
            add(new Label("groupName",group.getName()));
            add(new Label("groupDescription",group.getDescription()));
            add(new Button("joinButton"));
        }

        @Override
        public void onSubmit() {
            if (isUserLoggedIn()){
                Group group = businessService.joinGroup(getPageParameters().get(0).toString(), getSession().getUserId());
                getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("/Dashboard?groupId="+group.getId()));
            } else {
                Group group = businessService.searchGroup(getPageParameters().get(0).toString());
                getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("/SubscribeToGroup?groupCode="+group.getCode()));
            }
        }
    }

}
