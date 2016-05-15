package com.begentgroup.testappengine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class FacebookSignUpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FacebookInfo info = (FacebookInfo)req.getSession().getAttribute("FacebookInfo");
        if (info != null) {
            User user = new User();
            user.userName = req.getParameter("username");
            user.email = req.getParameter("email");
            user.registrationId = (String)req.getSession().getAttribute("RegistrationId");
            user.facebookId = info.id;
            try {
                user = DataManager.getInstance().addUser(user);
                req.getSession().removeAttribute("FacebookInfo");
                req.getSession().removeAttribute("RegistrationId");
                req.getSession().setAttribute("User", user);
                User respUser = user.clone();
                respUser.registrationId = null;
                Utility.responseSuccessMessage(resp, respUser);
                return;
            } catch (UserAddException e) {
                e.printStackTrace();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            Utility.responseErrorMessage(resp, "User add Fail");
            return;
        }
        Utility.responseErrorMessage(resp, "facebook not login");

    }
}
