package com.begentgroup.testappengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class ChatMessageClient {
    long senderId;
    String senderName;
    String senderEmail;
    String message;
    String date;

    public static List<ChatMessageClient> convertChatMessage(List<ChatMessage> chatlist) {
        List<ChatMessageClient> list = new ArrayList<>();
        for(ChatMessage chat : chatlist) {
            ChatMessageClient cmc = new ChatMessageClient();
            User sender = chat.sender.get();
            cmc.senderId = sender.id;
            cmc.senderName = sender.userName;
            cmc.senderEmail = sender.email;
            cmc.message = chat.message;
            cmc.date = Utility.convertDateToString(chat.date);
            list.add(cmc);
        }
        return list;
    }
}
