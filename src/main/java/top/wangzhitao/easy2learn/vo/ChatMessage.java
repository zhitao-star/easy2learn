package top.wangzhitao.easy2learn.vo;


import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
    private String userId;
    private String sessionId; // 会话ID
    private String userName;
    private String receiver; // 接收者
    private String content;  // 消息内容
    private Boolean userType;
    private Long currentOnlineUserCount;
    private Date messageTime;
}
