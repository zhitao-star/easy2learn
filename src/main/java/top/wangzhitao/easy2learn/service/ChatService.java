package top.wangzhitao.easy2learn.service;

import top.wangzhitao.easy2learn.vo.ChatMessage;

public interface ChatService {
    void onlineUser(ChatMessage chatMessage);

    void offlineUser(String sessionId);

    void sendMessage(String destination, String message);

    void getAllOnlineUser();

}
