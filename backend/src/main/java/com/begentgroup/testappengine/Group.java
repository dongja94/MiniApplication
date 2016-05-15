package com.begentgroup.testappengine;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-12.
 */
@Entity
public class Group implements Serializable {

    @Id public Long id;
    @Index public String groupName;
    public String description;
    public List<Ref<User>> members = new ArrayList<>();
}
