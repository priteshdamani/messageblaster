package com.pir.wicket.web.components;

import com.pir.domain.Group;
import com.pir.wicket.web.pages.GroupPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/9/13
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupPublicLink extends BookmarkablePageLink<Group> {

    public GroupPublicLink(String id, Group group) {
        super(id, GroupPage.class, new PageParameters());
        if (group == null){
            setVisible(false);
        } else {
            getPageParameters().set(0, group.getCode());
            setBody(Model.of("http://www.groupnotifier.com/Group/"+group.getCode()));
        }
    }
}
