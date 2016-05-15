package com.begentgroup.testappengine;

import com.googlecode.objectify.Ref;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-15.
 */
public class CommentAddServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("User");
        if (user != null) {
            Comment comment = new Comment();
            comment.comment = req.getParameter("comment");
            String contentid = req.getParameter("contentid");
            ImageContent content = DataManager.getInstance().getContent(Long.parseLong(contentid));
            comment.content = Ref.create(content);
            comment.writer = Ref.create(user);
            DataManager.getInstance().addComment(comment);
            Utility.responseSuccessMessage(resp, CommentResponse.convertComment(comment));
            return;
        }
        Utility.responseErrorMessage(resp, "not login");
    }
}
