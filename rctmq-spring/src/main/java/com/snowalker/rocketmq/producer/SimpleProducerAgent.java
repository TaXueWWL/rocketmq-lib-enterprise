package com.snowalker.rocketmq.producer;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.snowalker.rocketmq.RMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * @author snowalker
 * @date 2018/9/17
 * @desc 普通消息发送端
 */
@Scope("prototype")
@Service(value = "simpleProducerAgent")
public class SimpleProducerAgent {

    private Producer producer = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleProducerAgent.class);

    @Resource(name= "rmqConfig")
    RMQConfig rmqConfig;

    private String producerId;

    /**
     * Bean装配完成则初始化
     */
    public void initRMQProducer(String producerId) {
        Properties producerProperties = new Properties();
        this.producerId = producerId;
        producerProperties.setProperty(PropertyKeyConst.ProducerId, producerId);
        producerProperties.setProperty(PropertyKeyConst.AccessKey, rmqConfig.getAccessKey());
        producerProperties.setProperty(PropertyKeyConst.SecretKey, rmqConfig.getSecretKey());
        producerProperties.setProperty(PropertyKeyConst.ONSAddr, rmqConfig.getOnsAddr());
        producer = ONSFactory.createProducer(producerProperties);
        producer.start();
        LOGGER.info("Producer started，producerId=" + this.producerId);
    }

    /**
     * 外界直接传入Message
     * @param message
     * @return SendResult
     */
    public SendResult send(Message message) {
        return this.producer.send(message);
    }

    /**
     * 发送消息，Oneway 形式，服务器不应答，无法保证消息是否成功到达服务器；
     * 支持普通消息，发送定时/延时消息。
     * @param message
     */
    public void sendOneway(Message message) {
        this.producer.sendOneway(message);
    }

    /**
     * @param topic 主题
     * @param tag 标签
     * @param key 代表消息的业务关键属性，请尽可能全局唯一，如:订单号
     * @param msg 消息体
     */
    public SendResult send(String topic, String tag, String key, String msg) throws ONSClientException {
        Message message = new Message(topic, tag, key, msg.getBytes());
        return producer.send(message);
    }

    /**
     * 发送消息
     * @param topic 消息主题
     * @param tag  消息标签
     * @param msg 消息体
     */
    public SendResult send(String topic, String tag, String msg) {
        return this.send(topic, tag, "", msg);
    }

    /**
     * 发送消息，无标签
     * @param topic 消息主题
     * @param msg 消息体
     */
    public SendResult send(String topic, String msg) {
        return this.send(topic, "", msg);
    }

    /**
     * 发送消息，异步Callback形式；支持普通消息，发送定时/延时消息。
     * @param message
     * @param sendCallback
     */
    public void sendAsync(Message message, SendCallback sendCallback) {
        this.producer.sendAsync(message, sendCallback);
    }

    public void updateCredential(Properties credentialProperties) {
        if (this.producer != null) {
            this.producer.updateCredential(credentialProperties);
        }
    }

    public void setCallbackExecutor(final ExecutorService callbackExecutor) {
        this.producer.setCallbackExecutor(callbackExecutor);
    }

    /**
     * 检查服务是否已经启动。
     * @return
     */
    public boolean isStarted() {
        return this.producer.isStarted();
    }

    /**
     * 检查服务是否已经关闭。
     * @return
     */
    public boolean isClosed() {
        return this.producer.isClosed();
    }

    @PreDestroy
    public void shutdownRMQProducer() {
        if (this.producer != null) {
            this.producer.shutdown();
        }
    }

}
