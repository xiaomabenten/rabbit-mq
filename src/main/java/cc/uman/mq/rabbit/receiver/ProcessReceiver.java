package cc.uman.mq.rabbit.receiver;

import cc.uman.mq.notify.event.CommonApplicationEvent;
import cc.uman.mq.rabbit.config.CycleQueueConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * rabbitmq 消息消费者
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/7/18
 */
@Component
public class ProcessReceiver implements ChannelAwareMessageListener {

    @Autowired
    private ApplicationContext applicationContext;
    private static Logger logger = LoggerFactory.getLogger(ProcessReceiver.class);
    public static final String FAIL_MESSAGE = "This message will fail";

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            processMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果发生了异常，则将该消息重定向到缓冲队列，会在一定延迟之后自动重做
            channel.basicPublish(CycleQueueConfig.PER_QUEUE_TTL_EXCHANGE_NAME, CycleQueueConfig.DELAY_QUEUE_PER_QUEUE_TTL_NAME, null,
                    "The failed message will auto retry after a certain delay".getBytes());
        }
    }

    /**
     * 模拟消息处理。如果当消息内容为FAIL_MESSAGE的话，则需要抛出异常
     *
     * @param message
     * @throws Exception
     */
    public void processMessage(Message message) throws Exception {
        String realMessage = new String(message.getBody());
        // TODO: 2018/7/18 接收push的消息id，判断消息是否被删除
        logger.info("Received <" + realMessage + ">" + "收到的时间" + new Date());
        if (Objects.equals(realMessage, FAIL_MESSAGE)) {
            throw new Exception("Some exception happened");
        } else if (Objects.equals(realMessage, "Message From delay_queue_per_queue_ttl with expiration 4000")) {
            logger.debug("周期性数据");
        } else {
            applicationContext.publishEvent(new CommonApplicationEvent(this, Long.valueOf(realMessage)));
        }
    }
}
