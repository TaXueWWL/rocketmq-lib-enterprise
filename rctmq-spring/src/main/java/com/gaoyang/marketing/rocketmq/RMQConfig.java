package com.gaoyang.marketing.rocketmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author snowalker
 * @date 2018/9/16
 * @desc
 */
@Component(value = "rmqConfig")
public class RMQConfig {

    @Value("${spring.rocketmq.accessKey}")
    private String accessKey;

    @Value("${spring.rocketmq.secretkey}")
    private String secretKey;

    @Value("${spring.rocketmq.onsAddr}")
    private String onsAddr;

    @Value("${spring.rocketmq.msgTimeOutMillis}")
    private String msgTimeOutMillis = "0";

    @Value("${spring.rocketmq.messageModel}")
    private String messageModel;        // 订阅方式

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getOnsAddr() {
        return onsAddr;
    }

    public void setOnsAddr(String onsAddr) {
        this.onsAddr = onsAddr;
    }

    public String getMsgTimeOutMillis() {
        return msgTimeOutMillis;
    }

    public void setMsgTimeOutMillis(String msgTimeOutMillis) {
        this.msgTimeOutMillis = msgTimeOutMillis;
    }
}
