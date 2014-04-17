package com.pir.wicket.web.components;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */

public class BaseForm<T> extends Form<T> {

    public BaseForm(String id) {
        super(id);
    }

    public BaseForm(String id, IModel iModel) {
        super(id, iModel);
    }

    @Override
    public CustomSession getSession() {
        return (CustomSession) super.getSession();
    }
}