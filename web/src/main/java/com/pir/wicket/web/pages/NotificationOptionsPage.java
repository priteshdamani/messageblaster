package com.pir.wicket.web.pages;

import com.pir.domain.User;
import com.pir.wicket.web.components.BaseForm;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;

/**
 * Created by pritesh on 12/12/13.
 */
public class NotificationOptionsPage extends LoggedInBasePage {
    public NotificationOptionsPage(PageParameters parameters) {
        super(parameters);
        User user = getLoggedInUser();
        add(new OptionForm("notificationOptionForm",new CompoundPropertyModel<OptionModel>(new OptionModel(user.getId(),currentGroupId,user.isReceivingEmail(currentGroupId),user.isReceivingTxt(currentGroupId)))));
    }




    private class OptionForm extends BaseForm {

        private OptionForm(String id, IModel<OptionModel> userModel) {
            super(id, userModel);
            add(new CheckBox("receiveEmails"));
            add(new CheckBox("receiveTxt"));
        }

        @Override
        public void onSubmit() {
            OptionModel info = (OptionModel) getDefaultModelObject();
            businessService.notificationOptionsUpdate(info.getUserId(), info.getGroupId(),info.isReceiveEmails(),info.isReceiveTxt());
            getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("/Dashboard"));
        }
    }

    private class OptionModel implements Serializable {

        private Long userId;
        private Long groupId;

        private boolean receiveEmails;
        private boolean receiveTxt;

        private OptionModel(Long userId, Long groupId, boolean receiveEmails, boolean receiveTxt) {
            this.userId = userId;
            this.groupId = groupId;
            this.receiveEmails = receiveEmails;
            this.receiveTxt = receiveTxt;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public boolean isReceiveEmails() {
            return receiveEmails;
        }

        public void setReceiveEmails(boolean receiveEmails) {
            this.receiveEmails = receiveEmails;
        }

        public boolean isReceiveTxt() {
            return receiveTxt;
        }

        public void setReceiveTxt(boolean receiveTxt) {
            this.receiveTxt = receiveTxt;
        }
    }
}
