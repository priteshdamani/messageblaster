package com.pir.wicket.web.pages;

import com.pir.domain.Group;
import com.pir.wicket.web.components.PaidPaymentOptions;
import com.pir.wicket.web.components.UnPaidPaymentOptions;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentOptionsPage extends LoggedInBasePage {
    public PaymentOptionsPage(PageParameters parameters) {
        super(parameters);
        Group group = businessService.groupGet(currentGroupId);
        if (group.getSubscription().equals(Group.Subscription.FREE)) {
            Label lbl = new Label("subscriptionStatus", "<h2><i class='icon-money'></i> Simple Pricing </h2> </br> <h3>Just like our product, our pricing is also simple to understand. <br/><i class='icon-food'></i> Our price is the equivalent of buying 10 cups of coffee or donuts per year!</h3>");
            lbl.setEscapeModelStrings(false);
            add(lbl);
            add(new UnPaidPaymentOptions("paymentOption", parameters));
        } else {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
            Label lbl = new Label("subscriptionStatus", "You are currently on our PREMIUM package through " + formatter.print(group.getSubscriptionExpiresOn()));
            lbl.add(new AttributeAppender("style", "color:green;"));
            add(lbl);
            add(new PaidPaymentOptions("paymentOption", parameters));
        }
    }
}
