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
public class GroupAddServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("User");
        if (user != null) {
            Group group = new Group();
            group.groupName = req.getParameter("groupName");
            group.description = req.getParameter("description");
            group.members.add(Ref.create(user));
            try {
                DataManager.getInstance().addGroup(group);
                group.notificationKey = DeviceGroupManager.getInstance().createGroup(group);
                DataManager.getInstance().updateGroup(group);
                Utility.responseSuccessMessage(resp, GroupResponse.convertGroupResponse(group));
                return;
            } catch (GroupAddException e) {
                e.printStackTrace();
                Utility.responseErrorMessage(resp, "group name already exists");
            } catch (IOException e) {
                e.printStackTrace();
                Utility.responseErrorMessage(resp, "fail create device group");
            }
            return;
        }
        Utility.responseErrorMessage(resp, "not login");
    }
}
