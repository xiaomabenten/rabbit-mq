package cc.uman.mq.rabbit.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 及时发送mq
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/7/20
 */
@Component
public class SingleQueueConfig {

    /**
     * 发送一次性job任务
     */
    public static final String SINGLE_QUEUE_JOB_MESSAGE = "single_queue_job_message";

    /**
     * 重试消息队列
     */
    public static final String RETRY_QUEUE_MESSAGE = "retry_queue_message";


    @Bean
    Queue singleQueueJobMessage() {
        return QueueBuilder.durable(SINGLE_QUEUE_JOB_MESSAGE).build();
    }

    @Bean
    Queue retryQueueMessage() {
        return QueueBuilder.durable(RETRY_QUEUE_MESSAGE).build();
    }
}
