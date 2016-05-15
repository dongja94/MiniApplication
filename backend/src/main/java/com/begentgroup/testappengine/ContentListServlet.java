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
public class ContentListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ImageContent> list = DataManager.getInstance().getContents();
        Utility.responseSuccessMessage(resp, ImageContentResponse.convertContents(list));
    }
}
