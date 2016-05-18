package com.begentgroup.testappengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class ChatMessageClient {
    User sender;
    String message;
    String date;
    long groupId;

    public static List<ChatMessageClient> convertChatMessage(List<ChatMessage> chatlist) {
        List<ChatMessageClient> list = new ArrayList<>();
        for(ChatMessage chat : chatlist) {
            ChatMessageClient cmc = new ChatMessageClient();
            User sender = chat.sender.get();
            sender.password = null;
            sender.registrationId = null;
            cmc.sender = sender;
            cmc.message = chat.message;
            cmc.date = Utility.convertDateToString(chat.date);
            if (chat.group != null) {
                cmc.groupId = chat.group.getKey().getId();
            }
            list.add(cmc);
        }
        return list;
    }
}
