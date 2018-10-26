package com.snowalker.rocketmq.producer;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.bean.TransactionProducerBean;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.snowalker.rocketmq.RMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author snowalker
 * @date 2018/9/18
 * @desc
 */
@Scope("prototype")
@Service(value = "transactionProducerAgent")
public class TransactionProducerAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProducerAgent.class);

    @Resource(name = "rmqConfig")
    RMQConfig rmqConfig;

    /**
     * 需要注入该字段，指定构造{@code TransactionProducer}实例的属性，具体支持的属性详见{@link PropertyKeyConst}
     * @see TransactionProducerBean#setProperties(Properties)
     */
    private Properties properties;
    /**
     * 需要注入该字段，{@code TransactionProducer}在发送事务消息会依赖该对象进行事务状态回查
     * @see TransactionProducerBean#setLocalTransactionChecker(LocalTransactionChecker)
     */
    private LocalTransactionChecker localTransactionChecker;

    private TransactionProducer transactionProducer;

    /**生产者id*/
    private String producerId;

    /**
     * 初始化事务提供方
     * @param localTransactionChecker
     */
    public TransactionProducerAgent init(final LocalTransactionChecker localTransactionChecker, String producerId) {
        Properties properties = new Properties();
        this.producerId = producerId;
        properties.setProperty(PropertyKeyConst.ProducerId, this.producerId);
        properties.setProperty(PropertyKeyConst.AccessKey, rmqConfig.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, rmqConfig.getSecretKey());
        properties.setProperty(PropertyKeyConst.ONSAddr, rmqConfig.getOnsAddr());
        transactionProducer = ONSFactory.createTransactionProducer(properties,
                localTransactionChecker);
        LOGGER.info("TransactionProducerAgent started，producerId=" + this.producerId);
        return this;
    }

    /**
     * 启动该{@code TransactionProducer}实例，建议配置为Bean的init-method
     */
    public void start() {
        this.transactionProducer.start();
        LOGGER.info("TransactionProducerAgent started，producerId=" + this.producerId);
    }

    public SendResult send(Message message, LocalTransactionExecuter executer, Object arg) {
        return this.transactionProducer.send(message, executer, arg);
    }

    public void updateCredential(Properties credentialProperties) {
        if (this.transactionProducer != null) {
            this.transactionProducer.updateCredential(credentialProperties);
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public LocalTransactionChecker getLocalTransactionChecker() {
        return localTransactionChecker;
    }

    public void setLocalTransactionChecker(LocalTransactionChecker localTransactionChecker) {
        this.localTransactionChecker = localTransactionChecker;
    }

    public boolean isStarted() {
        return this.transactionProducer.isStarted();
    }

    public boolean isClosed() {
        return this.transactionProducer.isClosed();
    }

    /**
     * 关闭该{@code TransactionProducer}实例，建议配置为Bean的destroy-method
     */
    public void shutdown() {
        if (this.transactionProducer != null) {
            this.transactionProducer.shutdown();
        }
    }

    @PreDestroy
    public void shutdownRMQProducer() {
        this.shutdown();
    }

}
