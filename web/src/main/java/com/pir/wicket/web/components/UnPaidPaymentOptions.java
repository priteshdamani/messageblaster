package com.pir.wicket.web.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * Created by pritesh on 12/11/13.
 */
public class UnPaidPaymentOptions extends BasePanel {
    public UnPaidPaymentOptions(String id, PageParameters parameters) {
        super(id);
        StringValue groupId = parameters.get("groupId");
        Label payLbl = new Label("payLink", "<a class='btn btn-warning' href='https://www.groupnotifier.com/Pay?groupId=" + groupId + "'+><i class='icon-signin'></i>Upgrade Now</a>");
        payLbl.setEscapeModelStrings(false);
        add(payLbl);
    }
}
