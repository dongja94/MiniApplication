package com.begentgroup.testappengine;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class MessageListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("User");
        if (user != null) {
            String lastDateText = req.getParameter("lastDate");
            Date date;
            try {
                date = Utility.convertStringToDate(lastDateText);
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date(0);
            }
            List<ChatMessage> list = DataManager.getInstance().getChatMessage(user, date);
            Utility.responseSuccessMessage(resp, ChatMessageClient.convertChatMessage(list));
            return;
        }
        Utility.responseErrorMessage(resp, "Not login");
    }
}
