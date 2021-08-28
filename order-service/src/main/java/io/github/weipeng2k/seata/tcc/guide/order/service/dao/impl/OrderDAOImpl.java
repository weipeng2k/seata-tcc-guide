package io.github.weipeng2k.seata.tcc.guide.order.service.dao.impl;

import io.github.weipeng2k.seata.tcc.guide.order.service.dao.Order;
import io.github.weipeng2k.seata.tcc.guide.order.service.dao.OrderDAO;
import org.springframework.stereotype.Repository;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 * 单机本地Mock
 * </pre>
 *
 * @author weipeng2k 2021年08月27日 上午11:41:27
 */
@Repository("orderDAO")
public class OrderDAOImpl implements OrderDAO {

    /**
     * 订单存储
     */
    private final ConcurrentMap<Long, Order> orders = new ConcurrentHashMap<>();


    @Override
    public Long insertOrder(Order order) {
        sleepRandomIn(300);

        orders.put(order.getId(), order);
        return order.getId();
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orders.get(orderId);

        if (order != null) {
            sleepRandomIn(200);
            order.setEnable(false);
        }
    }

    @Override
    public void confirmOrder(Long orderId) {
        Order order = orders.get(orderId);

        if (order != null) {
            sleepRandomIn(200);
            order.setEnable(true);
        }
    }

    @Override
    public Order getOrder(Long orderId) {
        Order order = orders.get(orderId);
        sleepRandomIn(50);
        return order;
    }

    /**
     * 随机睡眠一段时间
     *
     * @param millis 毫秒
     */
    private void sleepRandomIn(int millis) {
        try {
            Thread.sleep(new Random().nextInt(millis) + 1);
        } catch (InterruptedException e) {
            // Ignore.
        }
    }
}
