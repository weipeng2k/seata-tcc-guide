package io.github.weipeng2k.seata.tcc.guide.order.service.impl;

import io.github.weipeng2k.seata.tcc.guide.order.api.OrderCreateService;
import io.github.weipeng2k.seata.tcc.guide.order.api.constants.OrderErrorCode;
import io.github.weipeng2k.seata.tcc.guide.order.api.exception.OrderException;
import io.github.weipeng2k.seata.tcc.guide.order.api.param.CreateOrderParam;
import io.github.weipeng2k.seata.tcc.guide.order.service.dao.Order;
import io.github.weipeng2k.seata.tcc.guide.order.service.dao.OrderDAO;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    @SuppressWarnings("ALL")
    public void createOrder(CreateOrderParam param, Long orderId) throws OrderException {
        if (param == null || param.getProductId() == null || param.getBuyerUserId() == null || param.getAmount() == null) {
            throw new OrderException(OrderErrorCode.ILLEGAL_ARGUMENT);
        }

        Order order = new Order();
        order.setBuyerUserId(param.getBuyerUserId());
        order.setProductId(param.getProductId());
        order.setAmount(param.getAmount());
        order.setId(orderId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long id = orderDAO.insertOrder(order);
        String content = String.format("买家{%d}购买商品{%d}，数量为{%d}，订单{%d}生成", order.getBuyerUserId(), order.getProductId(),
                order.getAmount(), id);
        String runtime = "@" + simpleDateFormat.format(
                new Date()) + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";
        // for eye-catching
        System.err.println(content + runtime);
    }

    @Override
    public void confirmOrder(BusinessActionContext businessActionContext) throws OrderException {
        Object orderIdStr = businessActionContext.getActionContext().get("orderId");
        if (orderIdStr != null) {
            Long orderId = Long.parseLong(orderIdStr.toString());
            Order order = orderDAO.getOrder(orderId);
            if (order != null && !order.isEnable()) {
                orderDAO.confirmOrder(orderId);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String content = String.format("买家{%d}购买商品{%d}，数量为{%d}，订单{%d}启用", order.getBuyerUserId(),
                        order.getProductId(), order.getAmount(), orderId);
                String runtime = "@" + simpleDateFormat.format(
                        new Date()) + "[" + Thread.currentThread().getName() + "] in Tx(" + businessActionContext.getXid() + ")";
                // for eye-catching
                System.err.println(content + runtime);
            }
        }
    }

    @Override
    public void cancelOrder(BusinessActionContext businessActionContext) throws OrderException {
        Object orderIdStr = businessActionContext.getActionContext().get("orderId");
        if (orderIdStr != null) {
            Long orderId = Long.parseLong(orderIdStr.toString());
            Order order = orderDAO.getOrder(orderId);
            if (order != null) {
                orderDAO.cancelOrder(orderId);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String content = String.format("买家{%d}购买商品{%d}，数量为{%d}，订单{%d}取消", order.getBuyerUserId(),
                        order.getProductId(), order.getAmount(), orderId);
                String runtime = "@" + simpleDateFormat.format(
                        new Date()) + "[" + Thread.currentThread().getName() + "] in Tx(" + businessActionContext.getXid() + ")";
                // for eye-catching
                System.err.println(content + runtime);
            }
        }
    }

}
