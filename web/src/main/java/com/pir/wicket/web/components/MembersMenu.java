package com.pir.wicket.web.components;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created by pritesh on 12/10/13.
 */
public class MembersMenu extends Panel {
    public MembersMenu(String id, Long groupId) {
        super(id);
        add(new MenuLink("menuSpan",groupId,"/Members","icon-eye-open","Members <br/>&nbsp; ","purple-background"));
    }
}
