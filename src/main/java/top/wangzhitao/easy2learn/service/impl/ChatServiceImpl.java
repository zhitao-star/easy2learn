package top.wangzhitao.easy2learn.service.impl;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import top.wangzhitao.easy2learn.constant.ChatConstant;
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


    @Resource
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public void onlineUser(ChatMessage chatMessage) {
        //将sessionID 和用户信息存入redis
        redisTemplate.opsForValue().set(ChatConstant.CHAT_SESSION + chatMessage.getSessionId(),JSON.toJSONString(chatMessage));
        //发送消息通知所有在线的用户  某人加入了进来
        SimpMessageHeaderAccessor reply = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        //返回当前用户信息
        byte[] messageBytes = JSON.toJSONString(chatMessage).getBytes();
        Message<byte[]> msg = new GenericMessage<>(messageBytes, reply.getMessageHeaders());
        simpMessagingTemplate.send("/online/onlineOrOfflineUser", msg);
    }

    @Override
    public void offlineUser(String sessionId) {
        //通过sessionId查找用户名
        String user = redisTemplate.opsForValue().get(ChatConstant.CHAT_SESSION + sessionId);
        ChatMessage userInfo = JSON.parseObject(user, ChatMessage.class);
        userInfo.setUserType(false);
        //发送消息通知所有在线的用户  某人退出了进来
        SimpMessageHeaderAccessor reply = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        //返回当前用户信息
        byte[] messageBytes = JSON.toJSONBytes(userInfo);
        Message<byte[]> msg = new GenericMessage<>(messageBytes, reply.getMessageHeaders());
        simpMessagingTemplate.send("/online/onlineOrOfflineUser", msg);
    }
}
