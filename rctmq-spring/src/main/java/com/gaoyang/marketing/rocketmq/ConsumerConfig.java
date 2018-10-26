package com.gaoyang.marketing.rocketmq;


/**
 * @author snowalker
 * @date 2018/9/17
 * @desc 消费者消费配置
 */
public class ConsumerConfig {

    public ConsumerConfig(String topic, String tag, String consumerId, String messageModel) {
        this.topic = topic;
        this.tag = tag;
        this.consumerId = consumerId;
        this.messageModel = messageModel;
    }

    private String topic;
    private String tag;
    // 消费者id
    private String consumerId;
    // 消息订阅方式,默认为集群方式--CLUSTERING，可选择广播模式：BROADCASTING
    private String messageModel;

    public String getConsumerId() {
        return consumerId;
    }

    public String getTopic() {
        return topic;
    }

    public String getTag() {
        return tag;
    }

    public String getMessageModel() {
        return messageModel;
    }
}
