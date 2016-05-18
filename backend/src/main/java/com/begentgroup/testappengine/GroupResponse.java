package com.begentgroup.testappengine;

import com.googlecode.objectify.Ref;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-18.
 */
public class GroupResponse {
    long id;
    String groupName;
    public String description;
    public List<User> members = new ArrayList<>();

    public static GroupResponse convertGroupResponse(Group group) {
        GroupResponse gr = new GroupResponse();
        gr.id = group.id;
        gr.groupName = group.groupName;
        gr.description = group.description;
        if (group.members != null) {
            for (Ref<User> u : group.members) {
                User user = u.get();
                user.registrationId = null;
                user.password = null;
                gr.members.add(user);
            }
        }
        return gr;
    }

    public static List<GroupResponse> convertGroupResponseList(List<Group> group) {
        List<GroupResponse> list = new ArrayList<>();
        if (group != null) {
            for (Group g : group) {
                list.add(convertGroupResponse(g));
            }
        }
        return list;
    }

}
