package cc.uman.mq.notify.listener;

import cc.uman.mq.notify.event.CommonApplicationEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

import java.util.EventObject;

/**
 * <p>
 * 所有消息监听者的父类，加入接收消息顺序
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/7/19
 */
public interface SmartApplicationListener<A extends EventObject> extends ApplicationListener<CommonApplicationEvent>, Ordered {
    /**
     * 如果实现支持该事件类型 那么返回true
     *
     * @param eventType
     * @return
     */
    Boolean supportsEventType(Class<? extends ApplicationEvent> eventType);

    /**
     * 如果实现支持“目标”类型，那么返回true
     *
     * @param sourceType
     * @return
     */
    Boolean supportsSourceType(Class<?> sourceType);

    /**
     * 顺序，即监听器执行的顺序，值越小优先级越高
     *
     * @return
     */
    @Override
    int getOrder();

}
