package com.begentgroup.testappengine;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.googlecode.objectify.Ref;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class SendMessageServlet extends HttpServlet {
    private static final String SERVER_KEY = "AIzaSyCN8HNXM4-qwaM0wTSiDt5WHu1wQiTxyJg";
    Sender sender = new Sender(SERVER_KEY);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("User");
        if (user != null) {
            String receiverId = req.getParameter("receiver");
            String msg = req.getParameter("message");
            User receiver = DataManager.getInstance().getUserById(Long.parseLong(receiverId));
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.sender = Ref.create(user);
            chatMessage.receiver = Ref.create(receiver);
            chatMessage.message = msg;
            DataManager.getInstance().addChatMessage(chatMessage);
            Message message = new Message.Builder().addData("type","chat")
                    .addData("sender", "" + user.id)
                    .addData("message","add message").build();
            com.google.android.gcm.server.Result result = sender.send(message, receiver.registrationId, 3);
            if (result.getMessageId() != null) {
                Utility.responseSuccessMessage(resp, "success");
                return;
            }
            Utility.responseErrorMessage(resp, "send fail");
            return;
        }
        Utility.responseErrorMessage(resp, "Not login");
    }
}
