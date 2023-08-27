package top.wangzhitao.easy2learn.service.impl;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.wangzhitao.easy2learn.constant.ChatConstant;
import top.wangzhitao.easy2learn.service.RedisService;
import top.wangzhitao.easy2learn.vo.ChatMessage;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class RedisServiceImpl implements RedisService {


    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Long countKeysWithPrefix() {
        return redisTemplate.opsForHash().size(ChatConstant.CHAT_SESSION);
    }

    @Override
    public ChatMessage getUserInfo(String sessionId) {
        String userInfo = (String) redisTemplate.opsForHash().get(ChatConstant.CHAT_SESSION, sessionId);
        return JSON.parseObject(userInfo, ChatMessage.class);
    }

    @Override
    public void onlineUser(ChatMessage chatMessage) {
        redisTemplate.opsForHash().put(ChatConstant.CHAT_SESSION, chatMessage.getSessionId(), JSON.toJSONString(chatMessage));
    }

    @Override
    public void offlineUser(ChatMessage chatMessage) {
        redisTemplate.opsForHash().delete(ChatConstant.CHAT_SESSION, chatMessage.getSessionId());
    }

    @Override
    public List<ChatMessage> getOnlineUser() {
        ArrayList<ChatMessage> list = new ArrayList<>();
        Map<Object, Object> allOnlineUser = redisTemplate.opsForHash().entries(ChatConstant.CHAT_SESSION);
        allOnlineUser.keySet().forEach(key -> {
            String sessionID = String.valueOf(key);
            ChatMessage chatMessage = JSON.parseObject((String) allOnlineUser.get(sessionID), ChatMessage.class);
            list.add(chatMessage);
        });
        return list;
    }
}
