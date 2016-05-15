package com.begentgroup.testappengine;

import com.google.gson.Gson;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class SignInServlet extends HttpServlet {

    Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String registrationId = req.getParameter("registrationId");
        try {
            User user = DataManager.getInstance().signin(email, password);
            if (user.registrationId == null || !user.registrationId.equals(registrationId)) {
                user.registrationId = registrationId;
                DataManager.getInstance().updateUser(user);
            }
            user.password = null;
            user.registrationId = null;
            req.getSession().setAttribute("User", user);
            Utility.responseSuccessMessage(resp, user);
            return;
        } catch (InvalidUserInfoException e) {
            e.printStackTrace();
        }
        Utility.responseErrorMessage(resp, "invalid user info");
    }
}
