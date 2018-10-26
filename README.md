# rocketmq-springboot-tool

本工具包提供对官方sdk的薄封装

## 概述

目前支持特性如下：如果对相关概念有疑问，参考官方文档
1. 普通消息的发送，包括顺序消息 

[发送普通消息（三种方式）](https://help.aliyun.com/document_detail/29547.html?spm=a2c4g.11186623.6.601.350e11bcs4BXyH)

[发送消息（多线程）](https://help.aliyun.com/document_detail/55385.html?spm=a2c4g.11186623.6.602.3b6d5fa635YXRO)

2. 事务消息的发送

[发送事务消息](https://help.aliyun.com/document_detail/29548.html?spm=a2c4g.11186623.6.605.7582292d6kWTFg)

[分布式事务消息生产者实例 TransactionProducer](https://help.aliyun.com/document_detail/86374.html?spm=a2c4g.11186623.2.12.655818ebiXEBXS)
    
    描述：该方法用来发送一条事务型消息。事务型消息发送分为三个步骤：
    
    本服务实现类首先发送一条半消息到到消息服务器；
    通过executer执行本地事务；
    根据上一步骤执行结果, 决定发送提交或者回滚第一步发送的半消息。
3. 延迟消息发送

[收发延时消息](https://help.aliyun.com/document_detail/29549.html?spm=a2c4g.11186623.2.17.4aa4afa85gVByS)
    
        msg.setStartDeliverTime(delayTime);
     
4. 消息的消费功能支持

[消息消费](https://help.aliyun.com/document_detail/49323.html?spm=a2c4g.11186623.6.604.427547db7R83wT)

## 使用样例

样例工程名为：rctmq-spring

应用需要是基于maven的springboot项目，maven依赖为：

        <dependency>
			<artifactId>rctmq-spring</artifactId>
			<groupId>com.snowalker</groupId>
			<version>1.0.0</version>
		</dependency>
### 0.配置文件[application.properties]
在springboot的application.properties中添加如下配置

        # 后端获取accesskey
        spring.rocketmq.accessKey=your-accesskey
        # 后台获取secret
        spring.rocketmq.secretkey=your-secretkey
        # 消息超时时间
        spring.rocketmq.msgTimeOutMillis=500000
        # 远端连接地址
        spring.rocketmq.onsAddr=http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet
        # 订阅模式：集群：PropertyValueConst.CLUSTERING 广播：BROADCASTING
        spring.rocketmq.messageModel=CLUSTERING

### 1. 事务消息生产者[TransactionProducerAgent]

> TransactionProducerAgent对事务消息发送做了薄封装。

0. 在需要使用消息事务的业务类中通过 **@Autowired** 引入TransactionProducerAgent

        @Autowired
        TransactionProducerAgent transactionProducerAgent;
    
1. 定义一个void方法，不能throw异常，注解为@PostConstruct，
进行事务消息生产者初始化。此处应当同时注入业务回查方法及生产者id


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
                    "PID-GYJX"
            ).start();

2. 业务方法中发送消息，需要调用者自己实现消本地业务的提交操作，这里可以使用匿名内部类或者编写新的
业务逻辑类并注入事务消息方法。

        /**0.事务消息，需要调用方自行序列化为消息协议，携带业务参数以便消费方进行业务操作*/
        String sendMsg = new ObjectMapper()
                .writeValueAsString(new Demo(name, "[事务消息测试]"));
        
        /**1.发送事务半消息，执行本地业务，这里只是个demo，具体的业务中，本地事务应当是一个Spring的service实体*/
        SendResult sendResult = transactionProducerAgent.send(new Message(topic, "WUWL", sendMsg.getBytes()),
                new LocalTransactionExecuter() {
                    @Override
                    public TransactionStatus execute(Message msg, Object arg) {
                        System.out.println("执行本地事务, 并根据本地事务的状态提交TransactionStatus.");
                        return TransactionStatus.CommitTransaction;
                    }
                }, null);

### 2. 普通消息生产者[SimpleProducerAgent]
> SimpleProducerAgent提供普通业务消息的发送能力。

1. 引入SimpleProducerAgent

在你的serviceBean里通过 **@Autowired** 注入即可

    @Autowired
    SimpleProducerAgent agent;

2. 当确定你的应用里只有一个生产者，那么可以在配置文件中指定topic，在业务中使用如下api发送消息


        /**
        * @param topic 主题
        * @param tag 标签
        * @param key 代表消息的业务关键属性，请尽可能全局唯一，如:订单号
        * @param msg 消息体
        */
        public SendResult send(String topic, String tag, String key, String msg) throws ONSClientException {
            Message message = new Message(topic, tag, key, msg.getBytes());
            return producer.send(message);
        }

        /**
        * 发送消息
        * @param topic 消息主题
        * @param tag  消息标签
        * @param msg 消息体
        */
        public SendResult send(String topic, String tag, String msg) {
            return this.send(topic, tag, "", msg);
        }

        /**
        * 发送消息，无标签
        * @param topic 消息主题
        * @param msg 消息体
        */
        public SendResult send(String topic, String msg) {
            return this.send(topic, "", msg);
        }

业务层生成对应的消息串，传入api即可（业务层需要提前序列化为JSON格式的消息体）
3. 如果你需要使用多个生产者，那么请使用**Message**进行消息的发送，自行指定topic

        /**
         * 外界直接传入Message
         * @param message
         * @return SendResult
         */
        public SendResult send(Message message) {
            return this.producer.send(message);
        }
    
## 3. 消息消费者[SimpleConsumerAgent]
> 提供消息消费能力，支持事务及一般消息
1. 在需要使用消息事务的业务类中通过 **@Autowired** 引入TransactionProducerAgent
2. 定义一个void方法，不能throw异常，注解为 **@PostConstruct**，进行消息消费者初始化。
注意，**ConsumerConfig** 中需要初始化消费者id


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
                            // 消息重试
                            System.out.println("重试次数:" + message.getReconsumeTimes());
                            return Action.ReconsumeLater;
                        }
                    }
            ).subscribe().start();
        }

初始化init()方法中设置消费实现类，可以是一个spring的service实体，也可以直接写一个匿名内部类。

消息的订阅和消费启动同步开启，使用链式写法。

完整的代码如下：

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
                            // 消息重试
                            System.out.println("重试次数:" + message.getReconsumeTimes());
                            return Action.ReconsumeLater;
                        }
                    }
            ).subscribe().start();
        }
    
    }


## update log
1. 版本号从0.0.1升级为1.0.0
2. 生产者、事物生产者、消费者中增加对应的producerId，consumerId的自定义
3. 生产者初始化时，需要同时指明生产者PID；同样，消费者初始化的参数ConsumerConfig现在需要
增加CID，**注意**，均为必填项！
