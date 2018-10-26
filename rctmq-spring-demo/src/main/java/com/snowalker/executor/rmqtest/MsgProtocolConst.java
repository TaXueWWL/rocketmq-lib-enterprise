package com.snowalker.executor.rmqtest;

/**
 * @author snowalker
 * @date 2018/9/23
 * @desc 消息枚举
 */
public enum MsgProtocolConst {

    // 默认环境
    PAYMENT("PAYMENT", "PAYMENT_CHARGE_TOPIC", "扣款消息", "MARKETING", "PID_PAYMENT_CHARGE", "CID_PAYMENT_CHARGE"),
    ROLLBACK("ROLLBACK", "ROLLBACK_TOPIC", "账户冲正消息", "MARKETING", "PID_ROLLBACK", "CID_ROLLBACK");

    private String msgName;
    private String msgTopic;
    private String msgDesc;
    private String msgTag;
    private String consumerId;
    private String producerId;

    private MsgProtocolConst(String msgName, String msgTopic, String msgDesc, String msgTag, String producerId, String consumerId) {
        this.msgName = msgName;
        this.msgTopic = msgTopic;
        this.msgDesc = msgDesc;
        this.msgTag = msgTag;
        this.producerId = producerId;
        this.consumerId = consumerId;
    }

    public String getMsgTag() {
        return msgTag;
    }

    public void setMsgTag(String msgTag) {
        this.msgTag = msgTag;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public String getMsgTopic() {
        return msgTopic;
    }

    public void setMsgTopic(String msgTopic) {
        this.msgTopic = msgTopic;
    }

    public String getMsgDesc() {
        return msgDesc;
    }

    public void setMsgDesc(String msgDesc) {
        this.msgDesc = msgDesc;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }
}
