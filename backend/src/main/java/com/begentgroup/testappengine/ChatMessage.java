package com.begentgroup.testappengine;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by dongja94 on 2016-05-13.
 */
@Entity
public class ChatMessage {

    @Id Long id;
    @Index Date date = new Date();
    @Index Ref<User> sender;
    @Index Ref<User> receiver;
    String message;
}
