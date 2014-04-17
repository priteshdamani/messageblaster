package com.pir.util;

import com.pir.domain.User;
import com.pir.domain.UserGroupLink;
import com.pir.dto.UserPublicDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by pritesh on 12/10/13.
 */
public class ConvertUtils {
    public static List<UserPublicDto> convert(Set<UserGroupLink> users, Long groupId) {
        List<UserPublicDto> dtoList = new ArrayList<UserPublicDto>();
        for(UserGroupLink link : users){
            User user = link.getUser();
            dtoList.add(new UserPublicDto(user.getId(), groupId, user.getName(), user.getEmail(), link.getRole(), user.isReceivingEmail(groupId), user.isReceivingTxt(groupId)));
        }
        return dtoList;
    }
}
