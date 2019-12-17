package com.xiaolyuh.mq.listener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xiaolyuh.constants.RabbitConstants;
import com.xiaolyuh.mq.message.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 死信队列处理消息
 *
 * @author yuhao.wang
 */
@Service
public class SendMessageListener {
    enum Action {
        ACCEPT,  // 处理成功
        RETRY,   // 可以重试的错误
        REJECT,  // 无需重试的错误
    }

    private final Logger logger = LoggerFactory.getLogger(SendMessageListener.class);

    @RabbitListener(queues = RabbitConstants.QUEUE_NAME_SEND_COUPON)
    public void process(SendMessage sendMessage, Channel channel, Message message) throws Exception {
        logger.info("[{}]处理死信队列消息队列接收数据，消息体：{}", RabbitConstants.QUEUE_NAME_DEAD_QUEUE, JSON.toJSONString(sendMessage));
        Action action = Action.ACCEPT;
        long tag = message.getMessageProperties().getDeliveryTag();
        System.out.println(tag);
        try {
            // 参数校验
            Assert.notNull(sendMessage, "sendMessage 消息体不能为NULL");
            // TODO 处理消息

            //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//multiple：是否批量确认.true:将一次性ack所有小于deliveryTag的消息。
        } catch (Exception e) {
            logger.error("MQ消息处理异常，消息体:{}", message.getMessageProperties().getCorrelationIdString(), JSON.toJSONString(sendMessage), e);
            // 根据异常种类决定是ACCEPT、RETRY还是 REJECT
            action = Action.RETRY;
        } finally {
            // 通过finally块来保证Ack/Nack会且只会执行一次
            if (action == Action.ACCEPT) {
                channel.basicAck(tag, true);    // 确认消息已经消费成功
                // 重试
            } else if (action == Action.RETRY) {
                channel.basicNack(tag, false, true);
                // 拒绝消息也相当于主动删除mq队列的消息
            } else {
                channel.basicReject(tag, false);
            }


        }
    }
}