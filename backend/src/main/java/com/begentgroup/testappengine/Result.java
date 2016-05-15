package com.begentgroup.testappengine;

/**
 * Created by dongja94 on 2016-05-11.
 */
public class Result<T> {
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    public static final int NEED_SIGNUP = 3;
    public int code;
    public T result;
    public String error;
}
