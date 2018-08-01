package cc.uman.mq;

import cc.uman.mq.rabbit.config.CycleQueueConfig;
import cc.uman.mq.rabbit.config.ExpirationMessagePostProcessor;
import cc.uman.mq.rabbit.config.SingleQueueConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqApplicationTests {

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

}
