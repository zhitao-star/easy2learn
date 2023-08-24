package top.wangzhitao.easy2learn.controller;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import top.wangzhitao.easy2learn.constant.ChatConstant;
import top.wangzhitao.easy2learn.service.ChatService;
import top.wangzhitao.easy2learn.vo.ChatMessage;

import javax.annotation.Resource;

@Controller
@Slf4j
public class ChatController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private ChatService chatService;


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        //得到消息接收者userID
        String receiverInfo = redisTemplate.opsForValue().get(ChatConstant.CHAT_SESSION + chatMessage.getReceiver());
        ChatMessage receiverMessage = JSON.parseObject(receiverInfo, ChatMessage.class);
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        String userDestination = "/online/receiveMessage/" + receiverMessage.getUserId();
        byte[] messageBytes = JSON.toJSONBytes(chatMessage); // You might need to specify a character encoding here
        Message<byte[]> msg = new GenericMessage<>(messageBytes, headerAccessor.getMessageHeaders());
        simpMessagingTemplate.send(userDestination, msg);

    }

    @MessageMapping("/chat.onlineOrOfflineUser")
    public void addUser(@Payload ChatMessage chatMessage,
                          SimpMessageHeaderAccessor headerAccessor) {
        //得到sessionID 存到redis里
        String sessionId = headerAccessor.getSessionId();
        chatMessage.setSessionId(sessionId);
        //通知在线用户
        chatService.onlineUser(chatMessage);
    }
}
