package com.begentgroup.gcm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-17.
 */
public class DeviceGroupMessage {
    public static final String OPERATION_CREATE = "create";
    public static final String OPERATION_ADD = "add";
    public static final String OPERATION_REMOVE = "remove";

    final String operation;
    final String notification_key_name;
    final String notification_key;
    final String[] registration_ids;

    public DeviceGroupMessage(Builder builder) {
        this.operation = builder.operation;
        this.notification_key = builder.notificationKey;
        this.notification_key_name = builder.notificationKeyName;
        List<String> list = builder.registrationIds;
        this.registration_ids = list.toArray(new String[list.size()]);
    }

    public String getOperation() {
        return operation;
    }

    public String getNotificationKeyName() {
        return notification_key_name;
    }

    public String getNotificationKey() {
        return notification_key;
    }

    public String[] getRegistrationIds() {
        return registration_ids;
    }

    public static class Builder {
        String operation;
        String notificationKeyName;
        String notificationKey;
        List<String> registrationIds = new ArrayList<>();
        public Builder(String operation) {
            if (operation.equals(OPERATION_CREATE) || operation.equals(OPERATION_ADD)
                    || operation.equals(OPERATION_REMOVE)) {
                this.operation = operation;
                return;
            }
            throw new IllegalArgumentException("invalid operation");
        }

        public Builder notificationKeyName(String notificationKeyName) {
            this.notificationKeyName = notificationKeyName;
            return this;
        }

        public Builder notificationKey(String notificationKey) {
            this.notificationKey = notificationKey;
            return this;
        }

        public Builder registrationId(String registrationId) {
            registrationIds.add(registrationId);
            return this;
        }

        public Builder registrationIds(List<String> registrationIds) {
            this.registrationIds.addAll(registrationIds);
            return this;
        }

        public Builder registrationIds(String[] registrationIds) {
            return registrationIds(Arrays.asList(registrationIds));
        }

        public DeviceGroupMessage build() {
            return new DeviceGroupMessage(this);
        }
    }
}
