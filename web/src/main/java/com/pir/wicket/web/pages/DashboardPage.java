package com.pir.wicket.web.pages;

import com.pir.domain.Message;
import com.pir.util.AppClock;
import com.pir.util.MessageBlasterConstants;
import com.pir.wicket.web.components.BaseForm;
import com.pir.wicket.web.components.NoMessagesLbl;
import com.pir.wicket.web.components.NotificationPanel;
import com.pir.wicket.web.components.SendMessageBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DashboardPage extends LoggedInBasePage {

    public DashboardPage(PageParameters parameters) {
        super(parameters);

        if (currentGroupId.equals(MessageBlasterConstants.UNSET))
        {
            throw new RedirectToUrlException("/Start");
        }

        SendMessageBox sendBox = new SendMessageBox("sendBox",currentGroupId);
        sendBox.setVisible(getLoggedInUser().isHigherRole(currentGroupId));
        add(sendBox);

        /*add(new SendMessageForm("sendForm", new CompoundPropertyModel<SendMessageModel>(new SendMessageModel())));*/

        NoMessagesLbl lbl = new NoMessagesLbl("noMessages");
        if (businessService.messagesGet(currentGroupId, 20).size() == 0){
            lbl.setVisible(true);
        } else {
            lbl.setVisible(false);
        }
        add(lbl);

        add(new MessageListView("messageList", new LoadableDetachableModel<List<Message>>() {
            @Override
            protected List<Message> load() {
                return businessService.messagesGet(currentGroupId, 20);
            }
        }));
    }


    private class MessageListView extends ListView<Message> {


        private MessageListView(String id, IModel<? extends List<? extends Message>> model) {
            super(id, model);
        }

        @Override
        protected void populateItem(ListItem<Message> item) {
            Message message = item.getModelObject();
            item.add(new Label("senderName",message.getCreatedBy().getName()));
            item.add(new Label("messageDate", AppClock.getAgoString(message.getCreatedDate())));
            item.add(new Label("subject",message.getSubject()));
            item.add(new Label("body",message.getBody()));
        }
    }

    private class SendMessageForm extends BaseForm {
        private NotificationPanel feedbackPanel;

        private SendMessageForm(String id, IModel<SendMessageModel> userModel) {
            super(id, userModel);
            /*feedbackPanel = new NotificationPanel("feedback");
            feedbackPanel.setOutputMarkupId(true);
            add(feedbackPanel);*/
            add(new TextField("subjectTxt"));
            add(new TextArea<String>("bodyTxt"));
            add(new Button("sendButton"));
        }

        @Override
        public void onSubmit() {
            SendMessageModel info = (SendMessageModel) getDefaultModelObject();
            Message message = businessService.sendMessage(getLoggedInUserId(),currentGroupId,info.getSubjectTxt(),info.getBodyTxt());
        }
    }

    private class SendMessageModel implements Serializable {
        private String subjectTxt;
        private String bodyTxt;

        public String getSubjectTxt() {
            return subjectTxt;
        }

        public void setSubjectTxt(String subjectTxt) {
            this.subjectTxt = subjectTxt;
        }

        public String getBodyTxt() {
            return bodyTxt;
        }

        public void setBodyTxt(String bodyTxt) {
            this.bodyTxt = bodyTxt;
        }
    }
}
