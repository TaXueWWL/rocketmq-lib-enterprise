package com.snowalker.executor.rmqtest.acm;

import com.alibaba.edas.acm.ConfigService;
import com.alibaba.edas.acm.listener.PropertiesListener;
import com.gaoyang.aliyun.acm.agent.AbstractAcmServer;
import com.gaoyang.aliyun.acm.agent.AcmAgent;
import com.gaoyang.aliyun.acm.agent.AcmConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @author snowalker
 * @version 1.0
 * @date 2018/10/25 9:55
 * @className SnowalkerAcmServer
 * @desc
 */
@Service
public class SnowalkerAcmServer extends AbstractAcmServer {

    @Autowired
    AcmConfig acmConfig;

    @PostConstruct
    @Override
    public void init() {
        AcmAgent.getInstance().init(acmConfig);
        // 配置更新监听
        ConfigService.addListener("cipher-snowalker", "DEFAULT_GROUP", new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                acmProperties = properties;
            }
        });
    }
}
