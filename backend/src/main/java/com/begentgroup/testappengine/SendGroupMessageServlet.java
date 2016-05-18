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
 * Created by dongja94 on 2016-05-18.
 */
public class SendGroupMessageServlet extends HttpServlet {
    private static final String SERVER_KEY = "AIzaSyBQ_qRcAzYrLgVSfiNWTkVeCIubJOovJRs";
    Sender sender = new Sender(SERVER_KEY);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("User");
        if (user != null) {
            String groupId = req.getParameter("groupId");
            String message = req.getParameter("message");
            Group group = DataManager.getInstance().getGroup(Long.parseLong(groupId));
            if (group.members != null) {
                boolean isMember = false;
                for (Ref<User> u : group.members) {
                    if (u.getKey().getId() == user.id) {
                        isMember = true;
                        break;
                    }
                }
                if (!isMember) {
                    Utility.responseErrorMessage(resp, "not group member");
                    return;
                }
                Ref<User> userRef = Ref.create(user);
                for (Ref<User> u : group.members) {
                    if (u.getKey().getId() != user.id) {
                        ChatMessage cm = new ChatMessage();
                        cm.sender = userRef;
                        cm.receiver = u;
                        cm.message = message;
                        DataManager.getInstance().addChatMessage(cm);
                    }
                }

                Message mm = new Message.Builder().addData("type","groupchat")
                        .addData("sender", "" + user.id)
                        .addData("groupId", "" + group.id)
                        .addData("message","group message").build();
                try {
                    com.begentgroup.gcm.Result result = sender.send(mm, group.notificationKey, 3);
                    if (result.getSuccess() != null && result.getSuccess() > 0) {
                        Utility.responseSuccessMessage(resp, "success");
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Utility.responseErrorMessage(resp, "send fail");
                return;
            }
            return;
        }
        Utility.responseErrorMessage(resp, "not login");
    }
}
