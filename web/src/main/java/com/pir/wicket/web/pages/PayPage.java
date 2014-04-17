package com.pir.wicket.web.pages;

import com.pir.dto.ChargeAttemptResultDto;
import com.pir.wicket.web.components.BaseForm;
import com.pir.wicket.web.components.NotificationPanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by pritesh on 12/10/13.
 */
public class PayPage extends LoggedInBasePage {

    private final Logger logger = LoggerFactory.getLogger(CreateNewGroupPage.class);

    public PayPage(PageParameters parameters) {
        super(parameters);
        if (!isUserLoggedIn()){
            getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("/Login"));
        } else {
            PayForm form = new PayForm("payForm", new CompoundPropertyModel<PayModel>(new PayModel(currentGroupId)));
            add(form);
        }
    }


    private class PayForm extends BaseForm {
        private NotificationPanel feedbackPanel;

        private PayForm(String id, IModel<PayModel> userModel) {
            super(id, userModel);
            feedbackPanel = new NotificationPanel("feedback");
            feedbackPanel.setOutputMarkupId(true);
            add(feedbackPanel);
            add(new HiddenField<Long>("groupId"));
            add(new TextField("ccNum"));
            add(new TextField("ccName"));
            add(new DropDownChoice("ccExpMon", Arrays.asList(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"})));
            add(new DropDownChoice("ccExpYear", Arrays.asList(new String[]{"2013","2014","2015","2016","2017","2018","2019","2020","2021","2022"})));
            add(new TextField("ccCvc"));
            Button payButton = new Button("payButton");
            payButton.add(new AttributeModifier("onClick","disable();"));
            add(payButton);
        }

        @Override
        public void onSubmit() {
            PayModel info = (PayModel) getDefaultModelObject();
            ChargeAttemptResultDto result = businessService.pay(getLoggedInUserId(), info.getGroupId(),info.getCcName(),info.getCcNum(),info.getCcExpMon(),info.getCcExpYear(),info.getCcCvc());
            if (result.isSuccess()){
                throw new RedirectToUrlException("/GenericMessage?title=Success&msg=Your payment is received, thank you :)");
            }else {
                error(result.getMessage());
            }
        }
    }

    private class PayModel implements Serializable{
        private Long groupId;
        private String ccNum;
        private String ccName;
        private String ccExpMon;
        private String ccExpYear;
        private String ccCvc;

        public PayModel(Long currentGroupId) {
            groupId = currentGroupId;
        }

        public String getCcNum() {
            return ccNum;
        }

        public void setCcNum(String ccNum) {
            this.ccNum = ccNum;
        }

        public String getCcName() {
            return ccName;
        }

        public void setCcName(String ccName) {
            this.ccName = ccName;
        }

        public String getCcExpMon() {
            return ccExpMon;
        }

        public void setCcExpMon(String ccExpMon) {
            this.ccExpMon = ccExpMon;
        }

        public String getCcExpYear() {
            return ccExpYear;
        }

        public void setCcExpYear(String ccExpYear) {
            this.ccExpYear = ccExpYear;
        }

        public String getCcCvc() {
            return ccCvc;
        }

        public void setCcCvc(String ccCvc) {
            this.ccCvc = ccCvc;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public void reset() {
            ccNum = null;
            ccName = null;
            ccExpMon = null;
            ccExpYear = null;
            ccCvc = null;
        }
    }


}
