package io.github.weipeng2k.seata.tcc.guide.order.service.impl;

import io.github.weipeng2k.seata.tcc.guide.order.api.OrderCreateService;
import io.github.weipeng2k.seata.tcc.guide.order.api.constants.OrderErrorCode;
import io.github.weipeng2k.seata.tcc.guide.order.api.exception.OrderException;
import io.github.weipeng2k.seata.tcc.guide.order.api.param.CreateOrderParam;
import io.github.weipeng2k.seata.tcc.guide.order.service.dao.Order;
import io.github.weipeng2k.seata.tcc.guide.order.service.dao.OrderDAO;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author weipeng2k 2021年08月25日 下午20:51:11
 */
@Service("orderCreateService")
@DubboService(interfaceClass = OrderCreateService.class, group = "dubbo", version = "1.0.0")
public class OrderCreateServiceImpl implements OrderCreateService {

    @Autowired
    private OrderDAO orderDAO;

    @Override
    public Long createOrder(CreateOrderParam param) throws OrderException {
        if (param == null || param.getProductId() == null || param.getBuyerUserId() == null || param.getAmount() == null) {
            throw new OrderException(OrderErrorCode.ILLEGAL_ARGUMENT);
        }

        Order order = new Order();
        order.setBuyerUserId(param.getBuyerUserId());
        order.setProductId(param.getProductId());
        order.setAmount(param.getAmount());

        Long id = orderDAO.insertOrder(order);
        String content = String.format("买家{%d}购买商品{%d}，数量为{%d}，订单{%d}生成", order.getBuyerUserId(), order.getProductId(),
                order.getAmount(), id);
        String runtime = "@" + new Date() + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";
        System.out.println(content + runtime);
//        businessActionContext.getActionContext().put("ORDER_ID", id);
        RootContext.entries().put("ORDER_ID", id);
        return id;
    }

    @Override
    public void confirmOrder(BusinessActionContext businessActionContext) throws OrderException {
        System.err.println("dddd");
        Long orderId = (Long) RootContext.entries().get("ORDER_ID");
        if (orderId != null) {
            Order order = orderDAO.getOrder(orderId);
            if (order != null) {
                orderDAO.confirmOrder(orderId);
                String content = String.format("买家{%d}购买商品{%d}，数量为{%d}，订单{%d}启用", order.getBuyerUserId(),
                        order.getProductId(), order.getAmount(), orderId);
                String runtime = "@" + new Date() + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";
                System.out.println(content + runtime);
            }
        }
    }

    @Override
    public void cancelOrder(BusinessActionContext businessActionContext) throws OrderException {
        System.err.println("xxxx");
        Long orderId = (Long) RootContext.entries().get("ORDER_ID");
        if (orderId != null) {
            Order order = orderDAO.getOrder(orderId);
            if (order != null) {
                orderDAO.cancelOrder(orderId);
                String content = String.format("买家{%d}购买商品{%d}，数量为{%d}，订单{%d}取消", order.getBuyerUserId(),
                        order.getProductId(), order.getAmount(), orderId);
                String runtime = "@" + new Date() + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";
                System.out.println(content + runtime);
            }
        }
    }

}
