package com.begentgroup.testappengine;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-15.
 */
public class CommentListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentid = req.getParameter("contentid");
        ImageContent content = DataManager.getInstance().getContent(Long.parseLong(contentid));
        if (content != null) {
            List<Comment> comments = DataManager.getInstance().getComments(content);
            Utility.responseSuccessMessage(resp, CommentResponse.convertComments(comments));
            return;
        }
        Utility.responseErrorMessage(resp, "invalid content");
    }
}
