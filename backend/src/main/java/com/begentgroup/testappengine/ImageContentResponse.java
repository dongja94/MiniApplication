package com.begentgroup.testappengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-15.
 */
public class ImageContentResponse {
    long id;
    long writerid;
    String content;
    String imageUrl;

    public static final List<ImageContentResponse> convertContents(List<ImageContent> contents) {
        List<ImageContentResponse> list = new ArrayList<>();
        for (ImageContent c : contents) {
            list.add(convertContent(c));
        }
        return list;
    }

    public static final ImageContentResponse convertContent(ImageContent c) {
        ImageContentResponse cr = new ImageContentResponse();
        cr.id = c.id;
        cr.writerid = c.writer.get().id;
        cr.content = c.content;
        cr.imageUrl = c.imageUrl;
        return cr;
    }
}
