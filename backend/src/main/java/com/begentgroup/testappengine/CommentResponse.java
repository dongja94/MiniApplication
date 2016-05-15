package com.begentgroup.testappengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-15.
 */
public class CommentResponse {
    long id;
    long writerid;
    long contentid;
    String comment;

    public static final List<CommentResponse> convertComments(List<Comment> comments) {
        List<CommentResponse> list = new ArrayList<>();
        for (Comment c : comments) {
            list.add(convertComment(c));
        }
        return list;
    }

    public static final CommentResponse convertComment(Comment c) {
        CommentResponse cr = new CommentResponse();
        cr.id = c.id;
        cr.writerid = c.writer.get().id;
        cr.contentid = c.content.get().id;
        cr.comment = c.comment;
        return cr;
    }
}
