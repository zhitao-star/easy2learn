package top.wangzhitao.easy2learn.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        log.info(String.valueOf(event.getTimestamp()));
        // 在这里处理连接关闭事件
        System.out.println("WebSocket连接已关闭：" + event.getMessage());
    }
}
