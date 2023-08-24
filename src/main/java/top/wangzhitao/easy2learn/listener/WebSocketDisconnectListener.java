package top.wangzhitao.easy2learn.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import top.wangzhitao.easy2learn.constant.ChatConstant;
import top.wangzhitao.easy2learn.service.ChatService;

import javax.annotation.Resource;

@Component
@Slf4j
public class WebSocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private ChatService chatService;
    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        // 在这里处理连接关闭事件
        String sessionId = event.getSessionId();
        //是断开
        chatService.offlineUser(sessionId);
        //删除会话
        redisTemplate.delete(ChatConstant.CHAT_SESSION + sessionId);

    }
}
