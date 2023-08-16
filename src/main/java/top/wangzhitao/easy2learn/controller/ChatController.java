package top.wangzhitao.easy2learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Controller;
import top.wangzhitao.easy2learn.vo.ChatMessage;

import javax.annotation.Resource;

@Controller
@Slf4j
public class ChatController {

    @Resource
    private SessionRepository<Session> sessionRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Session session = sessionRepository.findById(sessionId);
        log.info("chat.addUser:{}", sessionId);
        if (session != null) {
            // 将消息存入 Spring Session
            session.setAttribute("lastMessage", chatMessage.getContent());
            sessionRepository.save(session); // 保存会话数据
        }
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
