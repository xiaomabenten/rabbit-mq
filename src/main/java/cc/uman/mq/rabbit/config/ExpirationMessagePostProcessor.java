package cc.uman.mq.rabbit.config;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * <p>
 * 设置消息过期时间
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/7/18
 */
public class ExpirationMessagePostProcessor implements MessagePostProcessor {
    /**
     * 毫秒
     */
    private final Long ttl;

    public ExpirationMessagePostProcessor(Long ttl) {
        this.ttl = ttl;
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties()
                .setExpiration(ttl.toString());
        return message;
    }
}
