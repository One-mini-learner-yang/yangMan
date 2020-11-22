package com.yang.crowd.controller;

import com.yang.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
@Slf4j
@RestController
public class RedisController {
    @Autowired
    private StringRedisTemplate redisTemplate;


    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(String key, String value){
        try {
            redisTemplate.opsForValue().set(key,value);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
    @RequestMapping("/set/redis/key/value/with/timeout/remote")
    ResultEntity<String> setRedisKeyValueWithTimeOutRemote(@RequestParam("key") String key, @RequestParam("value") String value,@RequestParam("time") Long time, @RequestParam("timeUnit") TimeUnit timeUnit){
        log.info(key+value+time.toString());
        log.info(String.valueOf(timeUnit));
        try{
            redisTemplate.opsForValue().set(key,value,time,timeUnit);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisStringValueByKeyRemote(String key){
        try{
            String value=redisTemplate.opsForValue().get(key);
            return ResultEntity.successWithData(value);
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(String key){
        try {
            redisTemplate.delete(key);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
