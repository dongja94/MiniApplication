package com.begentgroup.testappengine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-18.
 */
public class UserInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("userId");
        if (!Utility.isEmpty(userid)) {
            User user = DataManager.getInstance().getUserById(Long.parseLong(userid));
            if (user != null) {
                user.password = null;
                user.registrationId = null;
                Utility.responseSuccessMessage(resp, user);
                return;
            }
        }
        Utility.responseErrorMessage(resp, "invalid userid");
    }
}
