package com.begentgroup.testappengine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class FacebookSignInServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("access_token");
        String registrationId = req.getParameter("registrationId");
        try {
            FacebookInfo info = FacebookLoginModule.getFacebookInfo(token);
            User user = DataManager.getInstance().getUserByFacebookId(info.id);
            if (user != null) {
                if (user.registrationId == null || !user.registrationId.equals(registrationId)) {
                    user.registrationId = registrationId;
                    DataManager.getInstance().updateUser(user);
                }
                req.getSession().setAttribute("User", user);
                User respUser = user.clone();
                respUser.password = null;
                respUser.registrationId = null;
                Utility.responseSuccessMessage(resp, respUser);
            } else {
                req.getSession().setAttribute("FacebookInfo", info);
                req.getSession().setAttribute("RegistrationId", registrationId);
                Utility.responseMessage(resp, Result.NEED_SIGNUP, info);
            }
            return;
        } catch (InvalidUserInfoException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Utility.responseErrorMessage(resp, "invalid token");
    }
}
