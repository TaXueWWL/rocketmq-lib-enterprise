package com.snowalker.executor.rmqtest;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaoyang.marketing.protocol.PaymentProtocol;
import com.gaoyang.marketing.protocol.RollbackProtocol;
import com.gaoyang.marketing.rocketmq.ConsumerConfig;
import com.gaoyang.marketing.rocketmq.consumer.SimpleConsumerAgent;
import com.gaoyang.marketing.rocketmq.producer.SimpleProducerAgent;
import com.gaoyang.marketing.rocketmq.producer.TransactionProducerAgent;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.Date;


/**
 * @author snowalker
 * @date 2018/9/17
 * @desc 发送延时消息
 *  // 延时消息，单位毫秒（ms），在指定延迟时间（当前时间之后）进行投递，例如消息在 3 秒后投递
    long delayTime = System.currentTimeMillis() + 3000;
    // 设置消息需要被投递的时间
    msg.setStartDeliverTime(delayTime);
 */
@Controller
public class MQSenderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQSenderController.class);

    @Autowired
    SimpleProducerAgent agent;

    @Autowired
    SimpleConsumerAgent simpleConsumerAgent;

    @Autowired
    TransactionProducerAgent transactionProducerAgent;

    @Autowired
    TransactionProducerAgent rollbackProducerAgent;



    @PostConstruct
    public void execute() {
        transactionProducerAgent.init(
                new LocalTransactionChecker() {
                    @Override
                    public TransactionStatus check(Message msg) {
                        System.out.println("收到事务消息的回查请求, MsgId: " + msg.getMsgID());
                        return TransactionStatus.CommitTransaction;
                    }
                },
                MsgProtocolConst.PAYMENT.getProducerId()
        ).start();

        rollbackProducerAgent.init(
                new LocalTransactionChecker() {
                    @Override
                    public TransactionStatus check(Message msg) {
                        System.out.println("ROLLBACK--收到事务消息的回查请求, MsgId: " + msg.getMsgID());
                        return TransactionStatus.CommitTransaction;
                    }
                },
                MsgProtocolConst.ROLLBACK.getProducerId()
        ).start();


        simpleConsumerAgent.init(new ConsumerConfig(
                RMQConstant.TOPIC,
                        "WUWL",
                        "CID-GYJX",
                        PropertyValueConst.CLUSTERING),
                new MessageListener() {
                    @Override
                    public Action consume(Message message, ConsumeContext context) {
                        System.out.println(new String(message.getBody()) + "---SUCCESS---111111111");
                        // 消息重试
                        System.out.println("重试次数:" + message.getReconsumeTimes());

                        String sendMsg = null;
                        try {
                            sendMsg = new ObjectMapper()
                                    .writeValueAsString(new Demo("snowalker武文良", "[事务消息测试]"));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }


                        SendResult sendResult = transactionProducerAgent.send(new Message(RMQConstant.TOPIC, "WUWL", sendMsg.getBytes()),
                                new LocalTransactionExecuter() {
                                    @Override
                                    public TransactionStatus execute(Message msg, Object arg) {
                                        System.out.println("执行本地事务, 并根据本地事务的状态提交TransactionStatus.");
                                        return TransactionStatus.CommitTransaction;
                                    }
                                }, null);

                        return Action.CommitMessage;
                    }
                }
        ).subscribe().start();
    }


    @ResponseBody
    @RequestMapping("sendTransaction")
    public String sendTransaction(HttpServletRequest request) throws Exception {

        String tradeId = request.getParameter("tradeId");

        PaymentProtocol paymentProtocol = new PaymentProtocol();

        paymentProtocol.setAccountId("021809270959470217011300099");
        paymentProtocol.setTradeAmountReal("1000.000");

        paymentProtocol.setOnlineProdId("1");
        paymentProtocol.setPhoneNum("15620974206");
        paymentProtocol.setProdId("1");
        paymentProtocol.setZfbUserId("17272717271");

        paymentProtocol.setTradeId(tradeId);
        paymentProtocol.setDeliverExtMsg("发货备注");
        paymentProtocol.setSiSupplierId("1");

        paymentProtocol.setTraceId("ac1e5355201809281809121060001");
        paymentProtocol.setUserId("021809241801230205010100001");
        paymentProtocol.setMsgName(MsgProtocolConst.PAYMENT.getMsgName());
        paymentProtocol.setTopicName(MsgProtocolConst.PAYMENT.getMsgTopic());


        String sendMsg = paymentProtocol.encode();
        System.out.println(sendMsg);

        SendResult sendResult = transactionProducerAgent.send(
                new Message(
                        MsgProtocolConst.PAYMENT.getMsgTopic(),
                        MsgProtocolConst.PAYMENT.getMsgTag(),
                        sendMsg.getBytes()),
                new LocalTransactionExecuter() {
                    @Override
                    public TransactionStatus execute(Message msg, Object arg) {
                        System.out.println("执行本地事务, 并根据本地事务的状态提交TransactionStatus.");
                        return TransactionStatus.CommitTransaction;
                    }
                }, null);

        return "success";
    }

    @ResponseBody
    @RequestMapping("sendPayment")
    public String sendPayment(HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        String sendMsg = new ObjectMapper()
                .writeValueAsString(new Demo(name, "[事务消息测试]"));


        SendResult sendResult = transactionProducerAgent.send(new Message(RMQConstant.TOPIC, "WUWL", sendMsg.getBytes()),
                new LocalTransactionExecuter() {
                    @Override
                    public TransactionStatus execute(Message msg, Object arg) {
                        System.out.println("执行本地事务, 并根据本地事务的状态提交TransactionStatus.");
                        return TransactionStatus.CommitTransaction;
                    }
                }, null);

        return "success";
    }

    @ResponseBody
    @RequestMapping("send")
    public String send(HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");

        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < 100; i++) {
            String sendMsg = mapper.writeValueAsString(
                    new Demo(name + i, i + "-years-old")
            );
            agent.send(RMQConstant.TOPIC, "WUWL", sendMsg);
            LOGGER.info("发送消息:" + "id=" + i);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("sendRollback")
    public String sendRollback(HttpServletRequest request) throws Exception {
        String tradeId = request.getParameter("tradeId");
        RollbackProtocol rollbackProtocol = new RollbackProtocol();
        rollbackProtocol.setUserId("021809261351450010010600001");
        rollbackProtocol.setTradeId(tradeId);
        rollbackProtocol.setTraceId("0000000000001");
        rollbackProtocol.setMsgName(MsgProtocolConst.ROLLBACK.getMsgName());
        rollbackProtocol.setTopicName(MsgProtocolConst.ROLLBACK.getMsgTopic());

        String sendMsg = rollbackProtocol.encode();
        System.out.println(sendMsg);

        SendResult sendResult = transactionProducerAgent.send(
                new Message(
                        MsgProtocolConst.ROLLBACK.getMsgTopic(),
                        MsgProtocolConst.ROLLBACK.getMsgTag(),
                        sendMsg.getBytes()),
                new LocalTransactionExecuter() {
                    @Override
                    public TransactionStatus execute(Message msg, Object arg) {
                        System.out.println("ROLLBACK--执行本地事务, 并根据本地事务的状态提交TransactionStatus.");
                        return TransactionStatus.CommitTransaction;
                    }
                }, null);

        return "success";
    }
}
