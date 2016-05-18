package com.begentgroup.testappengine;

import com.begentgroup.gcm.DeviceGroupSender;
import com.begentgroup.gcm.NotificationKey;
import com.googlecode.objectify.Ref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-18.
 */
public class DeviceGroupManager {
    DeviceGroupSender sender;

    private static DeviceGroupManager instance;
    public static DeviceGroupManager getInstance() {
        if (instance == null) {
            instance = new DeviceGroupManager();
        }
        return instance;
    }

    private DeviceGroupManager() {
        sender = new DeviceGroupSender(GcmConstant.SERVER_KEY, GcmConstant.SENDER_ID);
    }

    public String createGroup(Group group) throws IOException {
        List<String> registrationIds = new ArrayList<>();
        for (Ref<User> u : group.members) {
            User user = u.get();
            if (!Utility.isEmpty(user.registrationId)) {
                registrationIds.add(user.registrationId);
            }
        }
        NotificationKey key = sender.createDeviceGroup("" + group.id, registrationIds);
        return key.getNotificationKey();
    }

    public String addGroupMember(Group group, User user) throws IOException {
        if (Utility.isEmpty(user.registrationId)) throw  new IOException("empty registration id");
        NotificationKey key = sender.addDeviceGroup("" + group.id, group.notificationKey, user.registrationId);
        return key.getNotificationKey();
    }

    public String removeGroupMember(Group group, User user) throws IOException {
        if (Utility.isEmpty(user.registrationId)) throw  new IOException("empty registration id");
        NotificationKey key = sender.removeDeviceGroup("" + group.id, group.notificationKey, user.registrationId);
        return key.getNotificationKey();
    }
}
