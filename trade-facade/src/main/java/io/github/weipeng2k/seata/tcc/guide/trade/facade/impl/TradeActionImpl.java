package io.github.weipeng2k.seata.tcc.guide.trade.facade.impl;

import io.github.weipeng2k.seata.tcc.guide.order.api.OrderCreateService;
import io.github.weipeng2k.seata.tcc.guide.order.api.exception.OrderException;
import io.github.weipeng2k.seata.tcc.guide.order.api.param.CreateOrderParam;
import io.github.weipeng2k.seata.tcc.guide.trade.facade.TradeAction;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author weipeng2k 2021年08月25日 下午21:38:58
 */
@Component("tradeAction")
public class TradeActionImpl implements TradeAction {

    @DubboReference(group = "dubbo", version = "1.0.0")
    private OrderCreateService orderCreateService;

    @Override
    public void makeOrder(Long productId, Long buyerId, Integer amount) {
        CreateOrderParam createOrderParam = new CreateOrderParam();
        createOrderParam.setProductId(productId);
        createOrderParam.setBuyerUserId(buyerId);
        createOrderParam.setAmount(amount);

        try {
            Long orderId = orderCreateService.createOrder(createOrderParam);
        } catch (OrderException ex) {

        }
    }
}
