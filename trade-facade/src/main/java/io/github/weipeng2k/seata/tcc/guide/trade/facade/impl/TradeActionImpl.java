package io.github.weipeng2k.seata.tcc.guide.trade.facade.impl;

import io.github.weipeng2k.seata.tcc.guide.order.api.OrderCreateService;
import io.github.weipeng2k.seata.tcc.guide.order.api.exception.OrderException;
import io.github.weipeng2k.seata.tcc.guide.order.api.param.CreateOrderParam;
import io.github.weipeng2k.seata.tcc.guide.product.api.ProductInventoryService;
import io.github.weipeng2k.seata.tcc.guide.product.api.exception.ProductException;
import io.github.weipeng2k.seata.tcc.guide.product.api.param.OccupyProductInventoryParam;
import io.github.weipeng2k.seata.tcc.guide.trade.facade.TradeAction;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author weipeng2k 2021年08月25日 下午21:38:58
 */
@Component("tradeAction")
public class TradeActionImpl implements TradeAction {

    @DubboReference(group = "dubbo", version = "1.0.0")
    private OrderCreateService orderCreateService;
    @DubboReference(group = "dubbo", version = "1.0.0")
    private ProductInventoryService productInventoryService;

    @Override
    @GlobalTransactional
    public void makeOrder(Long productId, Long buyerId, Integer amount) {
        CreateOrderParam createOrderParam = new CreateOrderParam();
        createOrderParam.setProductId(productId);
        createOrderParam.setBuyerUserId(buyerId);
        createOrderParam.setAmount(amount);

        Long orderId;
        try {
            orderId = orderCreateService.createOrder(createOrderParam);
        } catch (OrderException ex) {
            throw new RuntimeException(ex);
        }

        OccupyProductInventoryParam occupyProductParam = new OccupyProductInventoryParam();
        try {
            occupyProductParam.setProductId(productId);
            occupyProductParam.setAmount(amount);
            occupyProductParam.setOutBizId(orderId);
            productInventoryService.occupyProductInventory(occupyProductParam);
        } catch (ProductException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setProductInventory(Long productId, Integer amount) {
        try {
            productInventoryService.setProductInventory(productId, amount);
        } catch (ProductException e) {
            e.printStackTrace();
        }
    }
}
