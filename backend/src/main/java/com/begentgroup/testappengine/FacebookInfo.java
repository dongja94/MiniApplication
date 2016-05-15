package com.begentgroup.testappengine;

import java.io.Serializable;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class FacebookInfo implements Serializable {
    public String id;
    public String name;
    public String email;
    public String profile;
    public FacebookError error;
}
