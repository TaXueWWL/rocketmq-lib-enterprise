package com.gaoyang.marketing.rocketmq.consumer;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.gaoyang.marketing.rocketmq.ConsumerConfig;
import com.gaoyang.marketing.rocketmq.RMQConfig;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author snowalker
 * @date 2018/9/17
 * @desc 普通消息接收端
 */
@Scope("prototype")
@Service
public class SimpleConsumerAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleConsumerAgent.class);

    @Resource(name= "rmqConfig")
    RMQConfig rmqConfig;

    /**手动方式初始化*/
    public SimpleConsumerAgent init(ConsumerConfig consumerConfig, MessageListener messageListener) {
        // 参数校验
        Preconditions.checkNotNull(consumerConfig);
        this.topic = consumerConfig.getTopic();
        this.tag = consumerConfig.getTag();
        this.messageModel = consumerConfig.getMessageModel();
        this.messageListener = messageListener;
        this.consumerId = consumerConfig.getConsumerId();
        // 配置加载
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.ConsumerId, this.consumerId);
        consumerProperties.setProperty(PropertyKeyConst.AccessKey, rmqConfig.getAccessKey());
        consumerProperties.setProperty(PropertyKeyConst.SecretKey, rmqConfig.getSecretKey());
        consumerProperties.setProperty(PropertyKeyConst.ONSAddr, rmqConfig.getOnsAddr());
        if (StringUtils.isEmpty(this.messageModel)) {
            // 本地订阅方式为空，默认集群
            consumerProperties.setProperty(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        } else {
            consumerProperties.setProperty(PropertyKeyConst.MessageModel, this.messageModel);
        }
        // 消费者初始化
        consumer = ONSFactory.createConsumer(consumerProperties);
        return this;
    }

    /**订阅消息*/
    public SimpleConsumerAgent subscribe() {
        if (null == this.consumer) {
            throw new ONSClientException("subscribe must be called after consumerBean started");
        }
        this.consumer.subscribe(this.topic, this.tag, this.messageListener);
        return this;
    }

    /**启动消费端*/
    public void start() {
        consumer.start();
        LOGGER.info("Consumer started，topic={}, consumerId={}, tag={}", topic, this.consumerId, tag);
    }

    private Consumer consumer = null;

    /**消息主题*/
    private String topic;

    /**消息标签*/
    private String tag;

    /**消息订阅方式,默认为集群方式--CLUSTERING，可选择广播模式：BROADCASTING*/
    private String messageModel;

    /**消费者id*/
    private String consumerId;

    /**消息监听器，接收到消息后的业务处理*/
    private MessageListener messageListener;

//    @PostConstruct
//    public void initRMQConsumer() {
//        Properties consumerProperties = new Properties();
//        consumerProperties.setProperty(PropertyKeyConst.ConsumerId, rmqConfig.getConsumerId());
//        consumerProperties.setProperty(PropertyKeyConst.AccessKey, rmqConfig.getAccessKey());
//        consumerProperties.setProperty(PropertyKeyConst.SecretKey, rmqConfig.getSecretKey());
//        consumerProperties.setProperty(PropertyKeyConst.ONSAddr, rmqConfig.getOnsAddr());
//        if (StringUtils.isEmpty(this.messageModel)) {
//            // 本地订阅方式为空，默认集群
//            consumerProperties.setProperty(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
//        } else {
//            consumerProperties.setProperty(PropertyKeyConst.MessageModel, this.messageModel);
//        }
//
//        consumer = ONSFactory.createConsumer(consumerProperties);
//        consumer.subscribe(this.topic, this.tag, this.messageListener);
//        consumer.start();
//        LOGGER.info("Consumer started，topic={}, consumerId={}, tag={}", topic, rmqConfig.getConsumerId(), tag);
//    }

    /**
     * 取消订阅某个主题
     * @param topic 消息主题
     */
    public void unsubscribe(String topic) {
        this.consumer.unsubscribe(topic);
    }

    @PreDestroy
    public void shutdownRMQConsumer() {
        if (this.consumer != null) {
            this.consumer.shutdown();
        }
    }
}
