package io.github.weipeng2k.seata.tcc.guide.order.service.impl;

import io.github.weipeng2k.seata.tcc.guide.order.api.OrderCreateService;
import io.github.weipeng2k.seata.tcc.guide.order.api.exception.OrderException;
import io.github.weipeng2k.seata.tcc.guide.order.api.param.CreateOrderParam;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author weipeng2k 2021年08月25日 下午20:51:11
 */
@Service("orderCreateService")
@DubboService(interfaceClass = OrderCreateService.class, group = "dubbo", version = "1.0.0")
public class OrderCreateServiceImpl implements OrderCreateService {
    @Override
    public Long createOrder(CreateOrderParam param) throws OrderException {
        if (param == null || param.getProductId() == null || param.getBuyerUserId() == null || param.getAmount() == null) {
            throw new OrderException("IllegalArgument");
        }

        return createOrderLogic(param);
    }

    private Long createOrderLogic(CreateOrderParam param) {
        System.out.println(
                "buyer:" + param.getBuyerUserId() + " create order for product:" + param.getProductId() + " and amount:" + param.getAmount());
        return System.currentTimeMillis();
    }
}
