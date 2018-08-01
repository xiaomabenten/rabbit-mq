package cc.uman.mq.notify.event;

import org.springframework.context.ApplicationEvent;

/**
 * <p>
 * 重试消息
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/7/31
 */
public class RetryApplicationEvent extends ApplicationEvent {

    /**
     * 消息推送轨迹id
     */
    private Long traceId;

    public RetryApplicationEvent(Object source, Long traceId) {
        super(source);
        this.traceId = traceId;
    }

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "RetryApplicationEvent{" +
                "traceId=" + traceId +
                ", source=" + source +
                '}';
    }
}
