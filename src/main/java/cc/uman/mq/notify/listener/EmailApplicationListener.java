package cc.uman.mq.notify.listener;

import cc.uman.mq.notify.event.CommonApplicationEvent;
import cc.uman.mq.notify.event.RetryApplicationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 邮件接收者
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/7/19
 */
@Async
@Component
public class EmailApplicationListener implements ApplicationListener {
    private static Logger logger = LoggerFactory.getLogger(EmailApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof CommonApplicationEvent) {
            CommonApplicationEvent commonApplicationEvent = (CommonApplicationEvent) applicationEvent;
            try {
                logger.info("Mail应用推送-接收到的消息：" + commonApplicationEvent.toString());
                // TODO: 2018/8/1 实现具体业务
            } catch (Exception e) {
                logger.error("Mail应用推送-消息转发失败。失败原因======>{}", e.getMessage());
            }
        }

        if (applicationEvent instanceof RetryApplicationEvent) {
            RetryApplicationEvent retryApplicationEvent = (RetryApplicationEvent) applicationEvent;
            logger.info("Mail重试推送应用推送-接收到的消息：" + retryApplicationEvent.toString());
            try {
                // TODO: 2018/8/1 实现具体业务
            } catch (Exception e) {
                logger.error("Mail重试推送-消息推送失败。失败原因======>{}", e.getMessage());
            }
        }
    }
}
