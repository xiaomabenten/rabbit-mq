# rabbit-mq
SpringBoot 实现rabbitmq 延时发送和及时发送，使用Spring事件处理机制实现任务分发开启子线程处理事务

### CycleQueueConfig
实现延时消息 两个交换机 和 一个消费者的 创建和绑定关系

### ExpirationMessagePostProcessor
实现MessagePostProcessor，设置消息过期时间

### 演示代码

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void contextLoads() {

    }

    @Test
    public void sendDelayedMq() {
        //发送延时消息队列，延迟10s
        rabbitTemplate.convertAndSend(CycleQueueConfig.DELAY_QUEUE_PER_MESSAGE_TTL_NAME, (Object) "121",
                new ExpirationMessagePostProcessor(10000L));
    }

    @Test
    public void sendSingledMq() {
        //发送及时消息队列
        rabbitTemplate.convertAndSend(SingleQueueConfig.SINGLE_QUEUE_JOB_MESSAGE, (Object) 1111L);
    }

    @Test
    public void deleteQueue(){
        //删除消息队列
        rabbitAdmin.deleteQueue(SingleQueueConfig.SINGLE_QUEUE_JOB_MESSAGE);
    }