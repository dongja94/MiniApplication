package com.begentgroup.testappengine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-18.
 */
public class GroupInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String groupid = req.getParameter("groupId");
        if (!Utility.isEmpty(groupid)) {
            Group group = DataManager.getInstance().getGroup(Long.parseLong(groupid));
            if (group != null) {
                Utility.responseSuccessMessage(resp, GroupResponse.convertGroupResponse(group));
                return;
            }
        }
        Utility.responseErrorMessage(resp, "invalid group id");
    }
}
