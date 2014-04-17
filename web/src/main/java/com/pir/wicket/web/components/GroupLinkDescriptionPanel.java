package com.pir.wicket.web.components;

import com.pir.domain.Group;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created by pritesh on 12/16/13.
 */
public class GroupLinkDescriptionPanel extends Panel {
    public GroupLinkDescriptionPanel(String id, Group group) {
        super(id);
        GroupPublicLink groupLink = new GroupPublicLink("groupPublicLink", group);
        add(groupLink);
        if (group == null){
            this.setVisible(false);
        }
    }
}
