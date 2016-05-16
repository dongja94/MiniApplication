package com.begentgroup.testappengine;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class FriendListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("User");
        if (user != null) {
            List<User> list = DataManager.getInstance().getUserList(user, null, 0, 10);
            Utility.responseSuccessMessage(resp, list);
            return;
        }
        Utility.responseErrorMessage(resp, "not login");
    }
}
