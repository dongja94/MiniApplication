package com.begentgroup.testappengine;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Created by dongja94 on 2016-05-10.
 */
@Entity
public class User implements Cloneable, Serializable {

    @Id public Long id;
    @Index public String userName;
    @Index public String password;
    @Index public String email;
    public String registrationId;
    @Index public String facebookId;

    @Override
    public User clone() throws CloneNotSupportedException {
        User user = (User)super.clone();
        user.id = id;
        user.userName = userName;
        user.password = password;
        user.email = email;
        user.registrationId = registrationId;
        return user;
    }
}
