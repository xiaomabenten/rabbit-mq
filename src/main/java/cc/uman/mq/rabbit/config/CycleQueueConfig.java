package cc.uman.mq.rabbit.config;

import cc.uman.mq.rabbit.receiver.ProcessReceiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 消息rabbitmq queue推送 定时推送
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/7/18
 */
@Component
public class CycleQueueConfig {
    /**
     * 发送到该队列的message会在一段时间后过期进入到delay_process_queue
     * 每个message可以控制自己的失效时间
     */
    public final static String DELAY_QUEUE_PER_MESSAGE_TTL_NAME = "delay_queue_per_message_ttl";
    /**
     * message失效后进入的队列，也就是实际的消费队列
     */
    public final static String DELAY_PROCESS_QUEUE_NAME = "delay_process_queue";

    /**
     * 路由到delay_queue_per_queue_ttl的exchange
     */
    public final static String PER_QUEUE_TTL_EXCHANGE_NAME = "per_queue_ttl_exchange";

    /**
     * 发送到该队列的message会在一段时间后过期进入到delay_process_queue
     * 队列里所有的message都有统一的失效时间
     */
    public final static String DELAY_QUEUE_PER_QUEUE_TTL_NAME = "delay_queue_per_queue_ttl";
    public final static int QUEUE_EXPIRATION = 4000;
    /**
     * DLX
     */
    private final static String DELAY_EXCHANGE_NAME = "delay_exchange";

    /**
     * 创建DLX exchange
     *
     * @return
     */
    @Bean
    DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE_NAME);
    }

    /**
     * 创建per_queue_ttl_exchange
     *
     * @return
     */
    @Bean
    DirectExchange perQueueTTLExchange() {
        return new DirectExchange(PER_QUEUE_TTL_EXCHANGE_NAME);
    }

    /**
     * 创建delay_queue_per_message_ttl队列
     * x-dead-letter-exchange声明了队列里的死信转发到的DLX名称，
     * x-dead-letter-routing-key声明了这些死信在转发时携带的routing-key名称
     *
     * @return
     */
    @Bean
    Queue delayQueuePerMessageTTL() {
        return QueueBuilder.durable(DELAY_QUEUE_PER_MESSAGE_TTL_NAME)
                // DLX，dead letter发送到的exchange
                .withArgument("x-dead-letter-exchange", DELAY_EXCHANGE_NAME)
                //dead letter携带的routing key
                .withArgument("x-dead-letter-routing-key", DELAY_PROCESS_QUEUE_NAME)
                .build();
    }

    /**
     * 创建delay_queue_per_queue_ttl队列
     *
     * @return
     */
    @Bean
    Queue delayQueuePerQueueTTL() {
        return QueueBuilder.durable(DELAY_QUEUE_PER_QUEUE_TTL_NAME)
                // DLX，dead letter发送到的exchange
                .withArgument("x-dead-letter-exchange", DELAY_EXCHANGE_NAME)
                //dead letter携带的routing key
                .withArgument("x-dead-letter-routing-key", DELAY_PROCESS_QUEUE_NAME)
                //设置队列过期时间
                .withArgument("x-message-ttl", QUEUE_EXPIRATION)
                .build();
    }

    /**
     * 创建delay_process_queue队列，也就是实际消费队列
     *
     * @return
     */
    @Bean
    Queue delayProcessQueue() {
        return QueueBuilder.durable(DELAY_PROCESS_QUEUE_NAME)
                .build();
    }

    /**
     * 将DLX绑定到实际消费队列
     *
     * @param delayProcessQueue
     * @param delayExchange
     * @return
     */
    @Bean
    Binding dlxBinding(Queue delayProcessQueue, DirectExchange delayExchange) {
        return BindingBuilder.bind(delayProcessQueue)
                .to(delayExchange)
                .with(DELAY_PROCESS_QUEUE_NAME);
    }

    /**
     * 将per_queue_ttl_exchange绑定到delay_queue_per_queue_ttl队列
     *
     * @param delayQueuePerQueueTTL
     * @param perQueueTTLExchange
     * @return
     */
    @Bean
    Binding queueTTLBinding(Queue delayQueuePerQueueTTL, DirectExchange perQueueTTLExchange) {
        return BindingBuilder.bind(delayQueuePerQueueTTL)
                .to(perQueueTTLExchange)
                .with(DELAY_QUEUE_PER_QUEUE_TTL_NAME);
    }

    /**
     * 定义delay_process_queue队列的Listener Container
     *
     * @param connectionFactory
     * @param processReceiver
     * @return
     */
    @Bean
    SimpleMessageListenerContainer processContainer(ConnectionFactory connectionFactory, ProcessReceiver processReceiver) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConnectionFactory(connectionFactory);
        //监听delay_process_queue
        container.setQueueNames(DELAY_PROCESS_QUEUE_NAME);
        container.setMessageListener(new MessageListenerAdapter(processReceiver));
        return container;
    }

    /**
     * 配置rabbitadmin 可以对消息队列进行操作
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
