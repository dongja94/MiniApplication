package com.begentgroup.testappengine;

import com.begentgroup.gcm.Message;
import com.begentgroup.gcm.Sender;
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
    Sender sender = new Sender(GcmConstant.SERVER_KEY);
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
            Message message = new Message.Builder().addData("type","simplechat")
                    .addData("sender", "" + user.id)
                    .addData("message","add message").build();
            try {
                com.begentgroup.gcm.Result result = sender.send(message, receiver.registrationId, 3);
                if (result.getMessageId() != null) {
                    Utility.responseSuccessMessage(resp, "success");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Utility.responseErrorMessage(resp, "send fail");
            return;
        }
        Utility.responseErrorMessage(resp, "Not login");
    }
}
