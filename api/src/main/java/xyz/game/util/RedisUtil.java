package xyz.game.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    public static final String PV = "PV";
    public static final String UV = "UV";

    private final StringRedisTemplate stringRedisTemplate;

    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // 设置字符串值
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    // 获取字符串值
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 设置字符串值并设置过期时间（单位：秒）
    public void setWithExpire(String key, String value, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    // 判断键是否存在
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    // 删除键
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    // 其他操作，如 List、Set、Hash 等操作可以类似地封装
    public void increment(String key) {
        stringRedisTemplate.opsForValue().increment(key);
    }

    public void setZero(String key) {
        stringRedisTemplate.opsForValue().set(key, "0");
    }

    public void addSet(String key, String value) {
        stringRedisTemplate.opsForSet().add(key, value);
    }

    public Long setCard(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    public void clearSet(String key) {
        stringRedisTemplate.delete(key);
    }

    // 获取分布式锁
    public boolean tryLock(String key, String value, long expireTime, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, value, expireTime, timeUnit));
    }

    // 释放分布式锁
    public void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}