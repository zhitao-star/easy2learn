package top.wangzhitao.easy2learn.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import javax.annotation.Resource;

@Component
@Slf4j
public class WebSocketConnectListener implements ApplicationListener<SessionConnectEvent> {


    @Resource
    private  RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {

    }
}
