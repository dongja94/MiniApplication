package com.begentgroup.testappengine;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by dongja94 on 2016-05-10.
 */
public class DataManager {

    private static DataManager instance;

    public static final DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    private DataManager() {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Group.class);
        ObjectifyService.register(ChatMessage.class);
        ObjectifyService.register(ImageContent.class);
        ObjectifyService.register(Comment.class);
    }

    public User addUser(User user) throws UserAddException {
        User alreadyUser = getUserByEmail(user.email);
        if (alreadyUser == null) {
            ofy().save().entity(user).now();
            return user;
        }
        throw new UserAddException("User exist");
    }

    public User getUserByEmail(String email) {
        try {
            User user = ofy().load().type(User.class).filter("email", email).first().now();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByFacebookId(String facebookId) {
        User user = ofy().load().type(User.class).filter("facebookId", facebookId).first().now();
        return user;
    }

    public User getUserById(long id) {
        try {
            User user = ofy().load().type(User.class).id(id).now();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getUserList(User user, String username, int offset, int limit) {
        List<User> list = new ArrayList<>();
        try {
            List<User> all = ofy().load().type(User.class).filter("id !=",user.id).offset(offset).limit(limit).list();
            for (User u : all) {
                if (username == null || username.equals("") || u.userName.contains(username)) {
                    u.password = null;
                    u.registrationId = null;
                    list.add(u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateUser(User user) {
        if (user.id != null) {
            ofy().save().entity(user).now();
        }
    }

    public User signin(String email, String password) throws InvalidUserInfoException {
        User user = ofy().load().type(User.class).filter("email", email).filter("password",password).first().now();
        if (user != null) {
            return user;
        }
        throw new InvalidUserInfoException("email or password invalid");
    }

    public Group addGroup(Group group) throws UserAddException {
        Group alreadyGroup = getGroup(group.groupName);
        if (alreadyGroup == null) {
            ofy().save().entity(group).now();
            return group;
        }
        throw new UserAddException("User exist");
    }

    public Group getGroup(String groupName) {
        Group group = ofy().load().type(Group.class).filter("groupName",groupName).first().now();
        return group;
    }

    public Group getGroup(long id) {
        Group group = ofy().load().type(Group.class).id(id).now();
        return group;
    }

    public List<Group> getGroup(User user) {
        List<Group> list = ofy().load().type(Group.class).filter("members", user).list();
        return list;
    }

    public List<Group> getGroupList(String groupName, int offset, int limit) {
        List<Group> list = new ArrayList<>();
        List<Group> all = ofy().load().type(Group.class).order("groupName").offset(offset).limit(limit).list();
        for (Group g : all) {
            if (g.groupName.contains(groupName)) {
                list.add(g);
            }
        }
        return list;
    }

    public void updateGroup(Group group) {
        if (group.id != null) {
            ofy().save().entity(group).now();
        }
    }

    public void deleteGroup(Group group) {
        if (group.id != null) {
            ofy().delete().entity(group).now();
        }
    }

    public void addChatMessage(ChatMessage message) {
        ofy().save().entity(message).now();
    }

    public List<ChatMessage> getChatMessage(User user, Date date) {
        List<ChatMessage> list = new ArrayList<>();
        list = ofy().load().type(ChatMessage.class).order("date").filter("date >", date).filter("receiver", user).list();
        return list;
    }

    public void addImageContent(ImageContent content) {
        ofy().save().entity(content).now();
    }

    public List<ImageContent> getContents() {
        List<ImageContent> list = ofy().load().type(ImageContent.class).list();
        return list;
    }

    public List<ImageContent> getContents(User user) {
        List<ImageContent> list = ofy().load().type(ImageContent.class).filter("writer", user).list();
        return list;
    }

    public ImageContent getContent(long id) {
        ImageContent content = ofy().load().type(ImageContent.class).id(id).now();
        return content;
    }

    public void addComment(Comment comment) {
        ofy().save().entity(comment).now();
    }

    public List<Comment> getComments(ImageContent content) {
        List<Comment> list = ofy().load().type(Comment.class).filter("content", content).list();
        return list;
    }

    public List<Comment> getComments(User user) {
        List<Comment> list = ofy().load().type(Comment.class).filter("writer", user).list();
        return list;
    }
}
