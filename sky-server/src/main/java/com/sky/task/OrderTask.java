package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    private OrderMapper orderMapper;

    @Autowired
    public OrderTask(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 每分钟执行一次，处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void updateTimeoutOrders() {
        List<Orders> orders = orderMapper.getByStatusAndLTTime(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason(MessageConstant.ORDER_TIME_OUT);
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    /**
     * 每日01:00:00执行一次，处理前一天处于待派送状态的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateDeliveryOrders() {
        List<Orders> orders = orderMapper.getByStatusAndLTTime(Orders.DELIVERY_IN_PROGRESS,
                LocalDateTime.now().plusMinutes(-60));

        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }
}
