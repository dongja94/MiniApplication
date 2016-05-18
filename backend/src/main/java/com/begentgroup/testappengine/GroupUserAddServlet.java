package com.begentgroup.testappengine;

import com.googlecode.objectify.Ref;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-18.
 */
public class GroupUserAddServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("User");
        if (user != null) {
            String groupId = req.getParameter("groupId");
            Group group = DataManager.getInstance().getGroup(Long.parseLong(groupId));
            if (group != null) {
                for(Ref<User> u : group.members) {
                    if (u.getKey().getId() == user.id) {
                        Utility.responseErrorMessage(resp, "already member");
                        return;
                    }
                }
                group.members.add(Ref.create(user));
                DataManager.getInstance().updateGroup(group);
                try {
                    DeviceGroupManager.getInstance().addGroupMember(group, user);
                    Utility.responseSuccessMessage(resp, GroupResponse.convertGroupResponse(group));
                } catch (IOException e) {
                    e.printStackTrace();
                    Utility.responseErrorMessage(resp, "fail add device group member");
                }
                return;
            }
            Utility.responseErrorMessage(resp, "invalid group id");
            return;
        }
        Utility.responseErrorMessage(resp, "not login");
    }
}
