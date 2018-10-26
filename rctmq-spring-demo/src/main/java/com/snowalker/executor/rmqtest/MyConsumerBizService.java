package com.snowalker.executor.rmqtest;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.springframework.stereotype.Service;

/**
 * @author snowalker
 * @date 2018/9/18
 * @desc
 */
@Service(value = "myConsumerBizService")
public class MyConsumerBizService implements MessageListener {

    @Override
    public Action consume(Message message, ConsumeContext context) {
        System.out.println(new String(message.getBody()) + "---SUCCESS---111111111");
        return Action.CommitMessage;
    }

}
