package com.xiaour.spring.boot.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaour.spring.boot.mq.ProducterService;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.Queue;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: huangxiangfei
 * @CreateDate: 2019/3/22$ 10:52$
 */
@RestController
public class MessageBackController {
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Resource
    private Queue queue;

    @RequestMapping(value = "/backFp", method = RequestMethod.POST)
    public void hello(@RequestParam String sendUrl, @RequestParam String data, @RequestParam String type) {
        JSONObject hashMap = new JSONObject();
        hashMap.put("data", data);
        hashMap.put("type", type);
        hashMap.put("sendUrl", sendUrl);
        ProducterService producterService = new ProducterService();
        producterService.sendMessage(hashMap.toString(), this.jmsMessagingTemplate, this.queue);
    }
}
