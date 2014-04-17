package com.pir.wicket.web.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * Created by pritesh on 12/11/13.
 */
public class GenericMessagePage extends GeneralPage {
    public GenericMessagePage(PageParameters parameters) {
        StringValue title = parameters.get("title");
        add(new Label("title",title.toString("")));
        StringValue msg = parameters.get("msg");
        add(new Label("msg",msg.toString("")));
    }
}
