package com.snowalker.executor.rmqtest;

/**
 * @author snowalker
 * @date 2018/9/23
 * @desc 消息枚举
 */
public enum MsgProtocolConst {

    // 开发环境
    PAYMENT_DEV("PAYMENT_DEV", "PAYMENT_CHARGE_TOPIC_DEV", "扣款消息", "MARKETING", "PID_PAYMENT_CHARGE_DEV", "CID_PAYMENT_CHARGE_DEV"),
    PAYMENT_RESULT_DEV("PAYMENT_RESULT_DEV", "PAYMENT_RESULT_TOPIC_DEV", "扣款结果消息", "MARKETING", "PID_PAYMENT_RESULT_DEV", "CID_PAYMENT_RESULT_DEV"),
    DELIVER_GOODS_DEV("DELIVER_GOODS_DEV", "DELIVER_GOODS_TOPIC_DEV", "发货消息", "MARKETING", "PID_DELIVER_GOODS_DEV", "CID_DELIVER_GOODS_DEV"),
    ROLLBACK_DEV("ROLLBACK_DEV", "ROLLBACK_TOPIC_DEV", "账户冲正消息", "MARKETING", "PID_ROLLBACK_DEV", "CID_ROLLBACK_DEV"),
    DELIVER_QUERY_TASK_DEV("DELIVER_QUERY_TASK_DEV", "DELIVER_QUERY_TASK_TOPIC_DEV", "掉单查询", "MARKETING", "PID_DELIVER_QUERY_TASK_DEV", "CID_DELIVER_QUERY_TASK_DEV"),
    TRADE_UPDATE_DEV("TRADE_UPDATE_DEV", "TRADE_UPDATE_TOPIC_DEV", "供货商通知核心更新订单状态", "MARKETING", "PID_TRADE_UPDATE_DEV", "CID_TRADE_UPDATE_DEV"),
    CREATE_TRADE_DEV("CREATE_TRADE_DEV", "CREATE_TRADE_TOPIC_DEV", "核心创建兑换订单", "MARKETING", "PID_CREATE_TRADE_DEV", "CID_CREATE_TRADE_DEV"),
    // 测试环境
    PAYMENT_TEST("PAYMENT_TEST", "PAYMENT_CHARGE_TOPIC_TEST", "扣款消息", "MARKETING", "PID_PAYMENT_CHARGE_TEST", "CID_PAYMENT_CHARGE_TEST"),
    PAYMENT_RESULT_TEST("PAYMENT_RESULT_TEST", "PAYMENT_RESULT_TOPIC_TEST", "扣款结果消息", "MARKETING", "PID_PAYMENT_RESULT_TEST", "CID_PAYMENT_RESULT_TEST"),
    DELIVER_GOODS_TEST("DELIVER_GOODS_TEST", "DELIVER_GOODS_TOPIC_TEST", "发货消息", "MARKETING", "PID_DELIVER_GOODS_TEST", "CID_DELIVER_GOODS_TEST"),
    ROLLBACK_TEST("ROLLBACK_TEST", "ROLLBACK_TOPIC_TEST", "账户冲正消息", "MARKETING", "PID_ROLLBACK_TEST", "CID_ROLLBACK_TEST"),
    DELIVER_QUERY_TASK_TEST("DELIVER_QUERY_TASK_TEST", "DELIVER_QUERY_TASK_TOPIC_TEST", "掉单查询", "MARKETING", "PID_DELIVER_QUERY_TASK_TEST", "CID_DELIVER_QUERY_TASK_TEST"),
    TRADE_UPDATE_TEST("TRADE_UPDATE_TEST", "TRADE_UPDATE_TOPIC_TEST", "供货商通知核心更新订单状态", "MARKETING", "PID_TRADE_UPDATE_TEST", "CID_TRADE_UPDATE_TEST"),
    CREATE_TRADE_TEST("CREATE_TRADE_TEST", "CREATE_TRADE_TOPIC_TEST", "核心创建兑换订单", "MARKETING", "PID_CREATE_TRADE_TEST", "CID_CREATE_TRADE_TEST"),
    // 正式环境
    PAYMENT_PROD("PAYMENT_PROD", "PAYMENT_CHARGE_TOPIC_PROD", "扣款消息", "MARKETING", "PID_PAYMENT_CHARGE_PROD", "CID_PAYMENT_CHARGE_PROD"),
    PAYMENT_RESULT_PROD("PAYMENT_RESULT_PROD", "PAYMENT_RESULT_TOPIC_PROD", "扣款结果消息", "MARKETING", "PID_PAYMENT_RESULT_PROD", "CID_PAYMENT_RESULT_PROD"),
    DELIVER_GOODS_PROD("DELIVER_GOODS_PROD", "DELIVER_GOODS_TOPIC_PROD", "发货消息", "MARKETING", "PID_DELIVER_GOODS_PROD", "CID_DELIVER_GOODS_PROD"),
    ROLLBACK_PROD("ROLLBACK_PROD", "ROLLBACK_TOPIC_PROD", "账户冲正消息", "MARKETING", "PID_ROLLBACK_PROD", "CID_ROLLBACK_PROD"),
    DELIVER_QUERY_TASK_PROD("DELIVER_QUERY_TASK_PROD", "DELIVER_QUERY_TASK_TOPIC_PROD", "掉单查询", "MARKETING", "PID_DELIVER_QUERY_TASK_PROD", "CID_DELIVER_QUERY_TASK_PROD"),
    TRADE_UPDATE_PROD("TRADE_UPDATE_PROD", "TRADE_UPDATE_TOPIC_PROD", "供货商通知核心更新订单状态", "MARKETING", "PID_TRADE_UPDATE_PROD", "CID_TRADE_UPDATE_PROD"),
    CREATE_TRADE_PROD("CREATE_TRADE_PROD", "CREATE_TRADE_TOPIC_PROD", "核心创建兑换订单", "MARKETING", "PID_CREATE_TRADE_PROD", "CID_CREATE_TRADE_PROD"),
    // 默认环境
    PAYMENT("PAYMENT", "PAYMENT_CHARGE_TOPIC", "扣款消息", "MARKETING", "PID_PAYMENT_CHARGE", "CID_PAYMENT_CHARGE"),
    PAYMENT_RESULT("PAYMENT_RESULT", "PAYMENT_RESULT_TOPIC", "扣款结果消息", "MARKETING", "PID_PAYMENT_RESULT", "CID_PAYMENT_RESULT"),
    DELIVER_GOODS("DELIVER_GOODS", "DELIVER_GOODS_TOPIC", "发货消息", "MARKETING", "PID_DELIVER_GOODS", "CID_DELIVER_GOODS"),
    ROLLBACK("ROLLBACK", "ROLLBACK_TOPIC", "账户冲正消息", "MARKETING", "PID_ROLLBACK", "CID_ROLLBACK"),
    DELIVER_QUERY_TASK("DELIVER_QUERY_TASK", "DELIVER_QUERY_TASK_TOPIC", "掉单查询", "MARKETING", "PID_DELIVER_QUERY_TASK", "CID_DELIVER_QUERY_TASK"),
    TRADE_UPDATE("TRADE_UPDATE", "TRADE_UPDATE_TOPIC", "供货商通知核心更新订单状态", "MARKETING", "PID_TRADE_UPDATE", "CID_TRADE_UPDATE"),
    CREATE_TRADE("CREATE_TRADE", "CREATE_TRADE_TOPIC", "核心创建兑换订单", "MARKETING", "PID_CREATE_TRADE", "CID_CREATE_TRADE");

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
