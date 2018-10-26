package com.snowalker.executor.rmqtest;

/**
 * @author snowalker
 * @date 2018/9/19
 * @desc
 */
public interface RMQConstant {

    String TOPIC = "RMQ_Test";

    /**消息tag*/
    String MSG_TAG_DEFAULT = "marketing";

    /**扣款消息topic订阅*/
    String PAYMENT_TOPIC_SUB = "PAYMENT_CHARGE_TOPIC";
    /**扣款完成topic订阅*/
    String PAYMENT_RESULT_TOPIC_PUB = "PAYMENT_RESULT_TOPIC";
    /**账户冲正topic*/
    String ROLLBACK_TOPIC_SUB = "ROLLBACK_TOPIC";
}
