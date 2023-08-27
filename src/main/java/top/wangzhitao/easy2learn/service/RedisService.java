package top.wangzhitao.easy2learn.service;

import top.wangzhitao.easy2learn.vo.ChatMessage;

import java.util.List;

public interface RedisService {

    Long countKeysWithPrefix();

    ChatMessage getUserInfo(String sessionId);

    void onlineUser(ChatMessage chatMessage);

    void offlineUser(ChatMessage chatMessage);

    List<ChatMessage> getOnlineUser();

}
