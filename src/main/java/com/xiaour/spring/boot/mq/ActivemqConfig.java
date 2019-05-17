package com.xiaour.spring.boot.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.Queue;

/**
 * @Author: huangxiangfei
 * @CreateDate: 2019/3/19$ 17:56$
 */
@Configuration
@EnableJms
public class ActivemqConfig {
    @Bean
    public Queue getQuese() {
        return new ActiveMQQueue("myactivemqmq");
    }


    @Value("${spring.activemq.user}")
    private String usrName;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

// 使用生产消息时配置
//@Value("${spring.activemq.queueName}")
//private String queueName;
//
//@Value("${spring.activemq.topicName}")
//private String topicName;

//@Bean
//public Queue queue(){
//    return new ActiveMQQueue(queueName);
//}
//
//@Bean
//public Topic topic(){
//    return new ActiveMQTopic(topicName);
//}

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(usrName, password, brokerUrl);
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        //设置为发布订阅方式, 默认情况下使用的生产消费者方式
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ActiveMQConnectionFactory connectionFactory) {
        return new JmsMessagingTemplate(connectionFactory);
    }
}

