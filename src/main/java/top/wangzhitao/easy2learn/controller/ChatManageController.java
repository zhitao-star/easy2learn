package top.wangzhitao.easy2learn.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wangzhitao.easy2learn.constant.ChatConstant;
import top.wangzhitao.easy2learn.service.RedisService;

import javax.annotation.Resource;

@RestController
public class ChatManageController {

    @Resource
    private RedisService redisService;

    @GetMapping("/getCurrentOnlineUsers")
    public Integer getCurrentOnlineUsers(){
        return redisService.countKeysWithPrefix(ChatConstant.CHAT_SESSION);
    }
}
