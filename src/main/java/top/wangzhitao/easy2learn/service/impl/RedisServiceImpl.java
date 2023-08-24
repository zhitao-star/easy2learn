package top.wangzhitao.easy2learn.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.wangzhitao.easy2learn.service.RedisService;

import javax.annotation.Resource;


@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public Integer countKeysWithPrefix(String prefix) {
        return redisTemplate.keys(prefix + "*").size();
    }
}
