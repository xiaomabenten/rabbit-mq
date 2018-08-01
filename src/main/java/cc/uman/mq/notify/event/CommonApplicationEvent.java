package cc.uman.mq.notify.event;

import org.springframework.context.ApplicationEvent;

/**
 * <p>
 * 应用消息推送监听方法
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/7/19
 */
public class CommonApplicationEvent extends ApplicationEvent {

    /**
     * 消息的id
     */
    private Long messageId;

    public CommonApplicationEvent(Object source, Long messageId) {
        super(source);
        this.messageId = messageId;
    }


    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "CommonApplicationEvent{" +
                "messageId=" + messageId +
                ", source=" + source +
                '}';
    }
}
