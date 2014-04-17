package com.pir.wicket.web.pages;

import com.pir.domain.Group;
import com.pir.domain.User;
import com.pir.util.MessageBlasterConstants;
import com.pir.wicket.web.components.*;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/6/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoggedInBasePage extends BasePage {

    protected Long currentGroupId = MessageBlasterConstants.UNSET;

    public LoggedInBasePage(PageParameters parameters) {

        add(new HeaderIncludes("headerIncludes"));
        add(new Footer("footer"));
        add(new FooterIncludes("footerIncludes"));
        add(new UserVoicePanel("userVoicePnl"));

        if (!isUserLoggedIn()) {
            throw new RedirectToUrlException("/Login");
        } else {
            final User user = getLoggedInUser();
            StringValue groupId = parameters.get("groupId");
            if (groupId.isNull() || groupId.isEmpty()) {
                if (!user.getGroups().isEmpty()) {
                    currentGroupId = user.getGroups().get(0).getId();
                }
            } else {
                currentGroupId = groupId.toLong();
            }

            MenuLink dashboardMenu = new MenuLink("dashboardMenu", currentGroupId, "/Dashboard", "icon-dashboard", "Dashboard <br/>&nbsp; ", "green-background");
            dashboardMenu.setVisible(!currentGroupId.equals(MessageBlasterConstants.UNSET));
            add(dashboardMenu);

            MenuLink messagesMenu = new MenuLink("messagesMenu", currentGroupId, "/Messages", "icon-envelope", "View All <br/>Messages ", "blue-background");
            messagesMenu.setVisible(!currentGroupId.equals(MessageBlasterConstants.UNSET));
            add(messagesMenu);

            MenuLink notificationOptionsMenu = new MenuLink("notificationOptionsMenu", currentGroupId, "/NotificationOptions", "icon-bullhorn", "Notification<br/>Options", "muted-background");
            notificationOptionsMenu.setVisible(!currentGroupId.equals(MessageBlasterConstants.UNSET));
            add(notificationOptionsMenu);

            MenuLink membersMenu = new MenuLink("membersMenu", currentGroupId, "/Members", "icon-eye-open", "Members <br/>&nbsp; ", "purple-background");
            membersMenu.setVisible(user.isAdminRole(currentGroupId) && !currentGroupId.equals(MessageBlasterConstants.UNSET));
            add(membersMenu);

            MenuLink subscriptionMenu = new MenuLink("subscriptionMenu", currentGroupId, "/PaymentOptions", "icon-money", "Subscription <br/>&nbsp; ", "orange-background");
            subscriptionMenu.setVisible(user.isAdminRole(currentGroupId) && !currentGroupId.equals(MessageBlasterConstants.UNSET));
            add(subscriptionMenu);

            MenuLink groupSettingsMenu = new MenuLink("groupSettingsMenu", currentGroupId, "/GroupSettings", "icon-cog", "Group <br/>Settings", "muted-background");
            groupSettingsMenu.setVisible(user.isAdminRole(currentGroupId) && !currentGroupId.equals(MessageBlasterConstants.UNSET));
            add(groupSettingsMenu);

            add(new LoggedInMenu("menu", user.getName(), user.getId(), user.isVerified()));

            add(new GroupSelectionView("groupNavigation", new LoadableDetachableModel<List<? extends Group>>() {
                @Override
                protected List<? extends Group> load() {
                    return user.getGroups();
                }
            }, DashboardPage.class));
            if (!currentGroupId.equals(MessageBlasterConstants.UNSET)) {
                Group group = businessService.groupGet(currentGroupId);
                Label lbl = new Label("dashboardName", group.getName() + " Dashboard <br/><i class='icon-phone'></i>&nbsp;Group number - " + group.getCode());
                lbl.setEscapeModelStrings(false);
                add(lbl);
                GroupLinkDescriptionPanel link = new GroupLinkDescriptionPanel("groupLink_div", group);
                if (!this.getClass().getName().toLowerCase().contains("dashboardpage")) {
                    link.setVisible(false);
                }
                add(link);
            } else {
                Label dashboardName = new Label("dashboardName", "User Profile");
                add(dashboardName);
                GroupLinkDescriptionPanel link = new GroupLinkDescriptionPanel("groupLink_div", null);
                link.setVisible(false);
                add(link);
            }
        }
    }


    private class GroupSelectionView extends ListView<Group> {

        private Class pageClass;

        private GroupSelectionView(String id, IModel<? extends List<? extends Group>> model, Class pageClass) {
            super(id, model);
            this.pageClass = pageClass;
        }

        @Override
        protected void populateItem(ListItem<Group> item) {
            Group group = item.getModelObject();
            PageParameters pageParameters = new PageParameters();
            pageParameters.add("groupId", group.getId());
            BookmarkablePageLink groupLink = new BookmarkablePageLink("groupLink", pageClass, pageParameters);
            groupLink.add(new Label("groupName", group.getName()));
            item.add(groupLink);
            item.add(new AttributeAppender("title", group.getName() + " Dashboard"));

            // if this is the current group for the page, then add an active class
            if (group.getId().equals(currentGroupId)) {
                item.add(new AttributeAppender("class", " active"));
            }
        }
    }
}
