package com.pir.wicket.web.components;

import com.pir.domain.Message;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

/**
 * Created by pritesh on 12/10/13.
 */
public class SendMessageBox extends BasePanel {

    public SendMessageBox(String id, final Long groupId) {
        super(id);

        Form sendForm = new Form("sendForm");

        TextField<String> subjectTxt = new TextField<String>("subjectTxt", new Model(""));
        sendForm.add(subjectTxt);
        subjectTxt.setRequired(false);
        TextArea<String> bodyTxt = new TextArea<String>("bodyTxt", new Model(""));
        bodyTxt.setRequired(false);
        sendForm.add(bodyTxt);

        sendForm.add(new AjaxSubmitLink("sendButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                String subject = form.get("subjectTxt").getDefaultModelObjectAsString();
                String body = form.get("bodyTxt").getDefaultModelObjectAsString();

                if (subject.equals("")) {
                    target.appendJavaScript("bootbox.alert({ message: \"Subject field is required!\" });");
                    return;
                }

                Message message = businessService.sendMessage(getLoggedInUserId(), groupId, subject, body);
                target.appendJavaScript("bootbox.alert({ message: \"Message Sent!\" });");
            }
        });

        add(sendForm);
    }
}
