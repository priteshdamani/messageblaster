package com.pir.wicket.web.pages;

import com.pir.domain.UserGroupLink;
import com.pir.dto.UserPublicDto;
import com.pir.exceptions.NotAnAdminException;
import com.pir.wicket.web.components.BaseForm;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class MembersPage extends LoggedInBasePage {
    private final Logger logger = LoggerFactory.getLogger(MembersPage.class);

    public MembersPage(PageParameters parameters) {
        super(parameters);
        add(new UserListView("membersList", new LoadableDetachableModel<List<UserPublicDto>>() {
            @Override
            protected List<UserPublicDto> load() {
                return businessService.groupMembersGet(currentGroupId);
            }
        }));

        DeleteForm deleteForm = new DeleteForm("deleteForm", new CompoundPropertyModel<DeleteModel>(new DeleteModel()));
        add(deleteForm);
        PrivilegeForm privilegeForm = new PrivilegeForm("privilegeLevelForm", new CompoundPropertyModel<PrivilegeModel>(new PrivilegeModel()));
        add(privilegeForm);
    }

    private class PrivilegeForm extends BaseForm {

        private PrivilegeForm(String id, CompoundPropertyModel<PrivilegeModel> model) {
            super(id, model);
            add(new HiddenField<Long>("userId"));
            add(new HiddenField<Long>("groupId"));
            add(new DropDownChoice<String>("privilegeLevel", UserGroupLink.RoleTypesGet()));
        }

        @Override
        public void onSubmit() {
            PrivilegeModel info = (PrivilegeModel) getDefaultModelObject();
            if (getSession().isLoggedIn()) {
                try {
                    businessService.privilegeChange(getLoggedInUserId(), info.getUserId(), info.getGroupId(), UserGroupLink.Role.valueOf(info.getPrivilegeLevel()));
                } catch (NotAnAdminException e) {
                    logger.error("Error changing priviledges", e);
                }
            }
        }
    }

    private class DeleteForm extends BaseForm {

        private DeleteForm(String id, CompoundPropertyModel<DeleteModel> model) {
            super(id, model);
            add(new HiddenField<Long>("userId"));
            add(new HiddenField<Long>("groupId"));
        }

        @Override
        public void onSubmit() {
            DeleteModel info = (DeleteModel) getDefaultModelObject();
            if (getSession().isLoggedIn()) {
                try {
                    businessService.deleteMembership(getLoggedInUserId(), info.getUserId(), info.getGroupId());
                } catch (NotAnAdminException e) {

                }
            }
        }
    }

    private class PrivilegeModel implements Serializable {
        private Long userId;
        private Long groupId;
        private String privilegeLevel;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public String getPrivilegeLevel() {
            return privilegeLevel;
        }

        public void setPrivilegeLevel(String privilegeLevel) {
            this.privilegeLevel = privilegeLevel;
        }
    }

    private class DeleteModel implements Serializable {
        private Long userId;
        private Long groupId;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }
    }


    private class UserListView extends ListView<UserPublicDto> {

        private UserListView(String id, IModel<? extends List<? extends UserPublicDto>> model) {
            super(id, model);
        }

        @Override
        protected void populateItem(ListItem<UserPublicDto> item) {
            UserPublicDto user = item.getModelObject();
            item.add(new HiddenField<Long>("userId", Model.of(user.getId())));
            item.add(new HiddenField<Long>("groupId", Model.of(currentGroupId)));
            item.add(new HiddenField<String>("username", Model.of(user.getName())));
            item.add(new Label("name", user.getName()));
            item.add(new Label("email", user.getEmail()));
            Label email = new Label("emailNotificationEnabled", "EMAIL");
            email.setVisible(user.isEmailEnabled());
            item.add(email);
            Label txt = new Label("txtNotificationEnabled", "TEXT");
            txt.setVisible(user.isTxtEnabled());
            item.add(txt);
            Label roleLbl = new Label("role", user.getRole().toString().replace("_", " "));
            if (user.getRole().equals(UserGroupLink.Role.ADMIN)) {
                roleLbl.add(new AttributeAppender("class", " label-danger"));
            } else if (user.getRole().equals(UserGroupLink.Role.CAN_SEND_MESSAGES)) {
                roleLbl.add(new AttributeAppender("class", " label-warning"));
            } else if (user.getRole().equals(UserGroupLink.Role.CAN_RECEIVE_MESSAGES)) {
                roleLbl.add(new AttributeAppender("class", " label-success"));
            }

            item.add(roleLbl);
        }
    }
}
