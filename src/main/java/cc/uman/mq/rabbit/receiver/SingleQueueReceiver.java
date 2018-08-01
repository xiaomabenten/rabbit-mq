package cc.uman.mq.rabbit.receiver;

import cc.uman.mq.notify.event.CommonApplicationEvent;
import cc.uman.mq.rabbit.config.SingleQueueConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 一次性消息队列处理器
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/7/20
 */
@Component
@RabbitListener(queues = SingleQueueConfig.SINGLE_QUEUE_JOB_MESSAGE)
public class SingleQueueReceiver {

    @Autowired
    private ApplicationContext applicationContext;

    @RabbitHandler
    public void process(Long jobId) {
        if (jobId != null) {
            applicationContext.publishEvent(new CommonApplicationEvent(this, jobId));
        }
    }
}
