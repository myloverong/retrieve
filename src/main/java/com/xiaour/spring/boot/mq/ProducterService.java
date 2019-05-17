package com.xiaour.spring.boot.mq;

import org.apache.activemq.ScheduledMessage;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: huangxiangfei
 * @CreateDate: 2019/3/19$ 18:19$
 */
@Service
public class ProducterService {

    public void sendMessage(String msg, JmsMessagingTemplate jmsMessagingTemplate, Queue queue) {
        jmsMessagingTemplate.convertAndSend(queue, msg);
    }

    public void sendMessage1(String msg, JmsMessagingTemplate jmsMessagingTemplate) {
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        try {
            //获取连接
            Connection connection = connectionFactory.createConnection();
            connection.start();
            //获取session
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            Destination destination = session.createQueue("myactivemq-backmq");
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage textMessage = session.createTextMessage(msg);
            //设置延迟时间
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 20000);
            //发送
            producer.send(textMessage);
            session.commit();
            producer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void sendMessage2(String msg, JmsMessagingTemplate jmsMessagingTemplate) {
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        try {
            //获取连接
            Connection connection = connectionFactory.createConnection();
            connection.start();
            //获取session
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            Destination destination = session.createQueue("myactivemq-backtwomq");
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage textMessage = session.createTextMessage(msg);
            //设置延迟时间
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 20000);
            //发送
            producer.send(textMessage);
            session.commit();
            producer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}