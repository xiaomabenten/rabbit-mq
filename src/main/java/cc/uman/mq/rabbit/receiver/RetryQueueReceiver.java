package cc.uman.mq.rabbit.receiver;

import cc.uman.mq.notify.event.RetryApplicationEvent;
import cc.uman.mq.rabbit.config.SingleQueueConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 消息重试 --- 传递push_message_trace 表id
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/7/31
 */
@Component
@RabbitListener(queues = SingleQueueConfig.RETRY_QUEUE_MESSAGE)
public class RetryQueueReceiver {
    @Autowired
    private ApplicationContext applicationContext;

    @RabbitHandler
    public void process(Long traceId) {
        if (traceId != null) {
            applicationContext.publishEvent(new RetryApplicationEvent(this, traceId));

        }
    }
}

