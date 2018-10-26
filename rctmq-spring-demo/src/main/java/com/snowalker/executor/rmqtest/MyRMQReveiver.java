package com.snowalker.executor.rmqtest;

import com.aliyun.openservices.ons.api.*;
import com.gaoyang.marketing.rocketmq.ConsumerConfig;
import com.gaoyang.marketing.rocketmq.consumer.SimpleConsumerAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author snowalker
 * @date 2018/9/17
 * @desc 消息重试要根据业务消息的id做幂等性处理
 * message.setKey("ORDERID_100");设置业务key
 * 消息幂等https://help.aliyun.com/document_detail/44397.html?spm=a2c4g.11186623.2.13.7dcf5b3aOHFGJt
 * 消息重试https://help.aliyun.com/document_detail/43490.html?spm=a2c4g.11186623.2.15.4ebb6b373lmdz6
 */
@Service(value = "myRMQReveiver")
public class MyRMQReveiver {


    @Resource(name = "myConsumerBizService")
    MyConsumerBizService bizService;

    @Autowired
    SimpleConsumerAgent agent;

    @PostConstruct
    public void execute() {
        agent.init(new ConsumerConfig(RMQConstant.TOPIC,
                        "WUWL",
                        "CID-GYJX",
                        PropertyValueConst.CLUSTERING),
                new MessageListener() {
                    @Override
                    public Action consume(Message message, ConsumeContext context) {
                        System.out.println(new String(message.getBody()) + "---SUCCESS---111111111");
//                        return Action.CommitMessage;
                        // 消息重试
                        System.out.println("重试次数:" + message.getReconsumeTimes());
                        return Action.CommitMessage;
                    }
                }
        ).subscribe().start();
    }

}
