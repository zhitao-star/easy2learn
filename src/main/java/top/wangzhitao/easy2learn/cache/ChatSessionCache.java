package top.wangzhitao.easy2learn.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatSessionCache {

    public static final Map<String, String> userSessionMap = new ConcurrentHashMap<>();
}
