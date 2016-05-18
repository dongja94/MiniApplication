package com.begentgroup.testappengine;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-18.
 */
public class MyGroupListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("User");
        if (user != null) {
            List<Group> groups = DataManager.getInstance().getGroupList(user);
            Utility.responseSuccessMessage(resp, GroupResponse.convertGroupResponseList(groups));
            return;
        }
        Utility.responseErrorMessage(resp, "not login");
    }
}
