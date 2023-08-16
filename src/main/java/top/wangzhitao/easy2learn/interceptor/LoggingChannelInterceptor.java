package top.wangzhitao.easy2learn.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingChannelInterceptor extends ChannelInterceptorAdapter {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("消息发送之前，message:{}",message.getHeaders().toString());
        return super.preSend(message, channel);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("消息发送之后，message:{}",message.getHeaders().toString());
        log.info("消息是否发送成功：{}",String.valueOf(sent));
        super.postSend(message, channel, sent);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        log.info("消息接受之前，message:{}",channel.toString());
        return super.preReceive(channel);
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        log.info("消息接受之后，message:{}",channel.toString());
        return super.postReceive(message, channel);
    }
}
