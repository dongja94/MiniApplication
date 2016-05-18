package com.begentgroup.testappengine;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by dongja94 on 2016-05-18.
 */
public class TestServlet extends HttpServlet {
    @Entity
    static class MyData {
        @Id Long id;
        @Index List<Ref<MyItem>> items = new ArrayList<>();

        @Override
        public String toString() {
            return items.toString();
        }
    }

    @Entity
    static class MyItem {
        @Id Long id;
        String item;

        @Override
        public String toString() {
            return item;
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectifyService.register(MyItem.class);
        ObjectifyService.register(MyData.class);
        MyItem item = new MyItem();
        item.item = "test";
        ofy().save().entity(item).now();
        MyData data = new MyData();
        data.items.add(Ref.create(item));
        ofy().save().entity(data).now();
        MyItem ii = ofy().load().type(MyItem.class).id(item.id).now();
        List<MyData> list = ofy().load().type(MyData.class).filter("items",ii).list();
        resp.setContentType("text/plain");
        resp.getWriter().print(list.toString());
    }
}
