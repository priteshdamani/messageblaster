package com.pir.wicket.web.pages;

import com.pir.domain.Message;
import com.pir.util.AppClock;
import com.pir.util.MessageBlasterConstants;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessagesPage extends LoggedInBasePage {
    public MessagesPage(PageParameters parameters) {
        super(parameters);
        add(new MessageListView("messageList",new LoadableDetachableModel<List<Message>>() {
            @Override
            protected List<Message> load() {
                return businessService.messagesGet(currentGroupId, MessageBlasterConstants.UNSET_PAGE_SIZE);
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
}
