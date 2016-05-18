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
public class GroupSearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("User");
        if (user != null) {
            String groupName = req.getParameter("query");
            String limitText = req.getParameter("limit");
            int limit = 30;
            if (!Utility.isEmpty(limitText)) {
                limit = Integer.parseInt(limitText);
            }
            String offsetText = req.getParameter("offset");
            int offset = 0;
            if (!Utility.isEmpty(offsetText)) {
                offset = Integer.parseInt(offsetText);
            }
            List<Group> grouplist = DataManager.getInstance().getGroupList(groupName, offset, limit);
            Utility.responseSuccessMessage(resp, GroupResponse.convertGroupResponseList(grouplist));
            return;
        }
        Utility.responseErrorMessage(resp, "not login");
    }
}
