package com.begentgroup.testappengine;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class Utility {
    static Gson gson = new Gson();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    public static Date convertStringToDate(String text) throws ParseException {
        return sdf.parse(text);
    }

    public static String convertDateToString(Date date) {
        return sdf.format(date);
    }

    public static <T> void responseMessage(HttpServletResponse resp, int code, T data) throws IOException {
        Result<T> result = new Result<>();
        result.code = code;
        result.result = data;
        resp.setContentType("application/json");
        resp.getWriter().print(gson.toJson(result));
    }
    public static <T> void responseSuccessMessage(HttpServletResponse resp, T data) throws IOException {
        responseMessage(resp, Result.SUCCESS, data);
    }

    public static <T>void responseErrorMessage(HttpServletResponse resp, T errorMessage) throws IOException {
        responseMessage(resp, Result.FAIL, errorMessage);
    }
}
