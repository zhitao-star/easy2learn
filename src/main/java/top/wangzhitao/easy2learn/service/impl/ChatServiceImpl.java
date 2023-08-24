package top.wangzhitao.easy2learn.service.impl;

import com.alibaba.fastjson2.JSON;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import top.wangzhitao.easy2learn.cache.ChatSessionCache;
import top.wangzhitao.easy2learn.constant.ChatConstant;
import top.wangzhitao.easy2learn.service.ChatService;
import top.wangzhitao.easy2learn.service.RedisService;

import javax.annotation.Resource;


@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private RedisService redisService;



    @Override
    public void onlineUser() {
        //发送消息通知所有在线的用户
        SimpMessageHeaderAccessor reply = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        String onlineUserNum = String.valueOf(redisService.countKeysWithPrefix(ChatConstant.CHAT_SESSION));
        byte[] messageBytes = onlineUserNum.getBytes(); // You might need to specify a character encoding here
        //返回数量+sessionId
        Message<byte[]> msg  = new GenericMessage<>(JSON.toJSONBytes(ChatSessionCache.userSessionMap), reply.getMessageHeaders());
        simpMessagingTemplate.send("/online/count", msg);
    }
}
