package com.begentgroup.testappengine;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by dongja94 on 2016-05-13.
 */
@Entity
public class ImageContent {
    @Id Long id;
    @Index Ref<User> writer;
    String content;
    String imageUrl;
}
