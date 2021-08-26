package io.github.weipeng2k.seata.tcc.guide.order.service.impl;

import io.github.weipeng2k.seata.tcc.guide.order.api.OrderCreateService;
import io.github.weipeng2k.seata.tcc.guide.order.api.constants.OrderErrorCode;
import io.github.weipeng2k.seata.tcc.guide.order.api.exception.OrderException;
import io.github.weipeng2k.seata.tcc.guide.order.api.param.CreateOrderParam;
import io.seata.core.context.RootContext;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author weipeng2k 2021年08月25日 下午20:51:11
 */
@Service("orderCreateService")
@DubboService(interfaceClass = OrderCreateService.class, group = "dubbo", version = "1.0.0")
public class OrderCreateServiceImpl implements OrderCreateService {

    /**
     * 订单存储
     */
    private ConcurrentMap<Long, Order> orders = new ConcurrentHashMap<>();
    // fake id generator
    private AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Long createOrder(CreateOrderParam param) throws OrderException {
        if (param == null || param.getProductId() == null || param.getBuyerUserId() == null || param.getAmount() == null) {
            throw new OrderException(OrderErrorCode.ILLEGAL_ARGUMENT);
        }

        try {
            Thread.sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
            // Ignore.
        }

        Long id = idGenerator.getAndIncrement();
        Order order = new Order();
        order.setId(id);
        order.setBuyerUserId(param.getBuyerUserId());
        order.setProductId(param.getProductId());
        order.setAmount(param.getAmount());

        orders.put(id, order);
        String begin = String.format("买家{%d}购买商品{%d}，数量为{%d}，订单{%d}生成", param.getBuyerUserId(), param.getProductId(),
                param.getAmount(), id);
        String runtime = "@" + new Date() + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";
        System.out.println(begin + runtime);
        return id;
    }

    @Override
    public void confirmOrder(Long orderId) throws OrderException {
        if (orderId == null) {
            throw new OrderException(OrderErrorCode.ILLEGAL_ARGUMENT);
        }

        Order order = orders.get(orderId);

        if (order != null) {
            try {
                Thread.sleep(new Random().nextInt(500));
            } catch (InterruptedException e) {
                // Ignore.
            }

            String begin = String.format("买家{%d}购买商品{%d}，数量为{%d}，订单{%d}启用", order.getBuyerUserId(), order.getProductId(),
                    order.getAmount(), orderId);
            String runtime = "@" + new Date() + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";
            System.out.println(begin + runtime);

            order.setEnable(true);
        }
    }

    @Override
    public void cancelOrder(Long orderId) throws OrderException {
        if (orderId == null) {
            throw new OrderException(OrderErrorCode.ILLEGAL_ARGUMENT);
        }

        Order order = orders.get(orderId);

        if (order != null) {
            try {
                Thread.sleep(new Random().nextInt(500));
            } catch (InterruptedException e) {
                // Ignore.
            }

            String begin = String.format("买家{%d}购买商品{%d}，数量为{%d}，订单{%d}取消", order.getBuyerUserId(), order.getProductId(),
                    order.getAmount(), orderId);
            String runtime = "@" + new Date() + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";
            System.out.println(begin + runtime);

            order.setEnable(false);
        }
    }

}
