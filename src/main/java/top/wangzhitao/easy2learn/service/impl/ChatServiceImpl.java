package top.wangzhitao.easy2learn.service.impl;

import com.alibaba.fastjson2.JSON;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import top.wangzhitao.easy2learn.service.ChatService;
import top.wangzhitao.easy2learn.service.RedisService;
import top.wangzhitao.easy2learn.vo.ChatMessage;

import javax.annotation.Resource;


@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private RedisService redisService;


    @Override
    public void onlineUser(ChatMessage chatMessage) {
        //将sessionID 和用户信息存入redis
        redisService.onlineUser(chatMessage);
        //发送消息通知所有在线的用户  某人加入了进来
        SimpMessageHeaderAccessor reply = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        //返回当前用户信息
        //查找当前在线用户数量
        chatMessage.setCurrentOnlineUserCount(redisService.countKeysWithPrefix());
        this.sendMessage("/online/onlineOrOfflineUser", JSON.toJSONString(chatMessage));
        this.getAllOnlineUser();
    }

    @Override
    public void offlineUser(String sessionId) {
        //通过sessionId查找用户名
        ChatMessage userInfo = redisService.getUserInfo(sessionId);
        userInfo.setUserType(false);
        //移出用户
        redisService.offlineUser(userInfo);
        userInfo.setCurrentOnlineUserCount(redisService.countKeysWithPrefix());
        //发送消息通知所有在线的用户  某人退出了进来
        this.sendMessage("/online/onlineOrOfflineUser", JSON.toJSONString(userInfo));
        this.getAllOnlineUser();
    }

    @Override
    public void sendMessage(String destination, String message) {
        SimpMessageHeaderAccessor reply = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        //返回当前用户信息
        Message<byte[]> msg = new GenericMessage<>(message.getBytes(), reply.getMessageHeaders());
        simpMessagingTemplate.send(destination, msg);
    }

    @Override
    public void getAllOnlineUser() {
        // TODO: 2023/8/27 优化返回的数据结构 
        this.sendMessage("/online/onlineOrOfflineUserList",JSON.toJSONString(redisService.getOnlineUser()));
    }
}
