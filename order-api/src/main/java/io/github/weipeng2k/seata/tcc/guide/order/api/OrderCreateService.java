package io.github.weipeng2k.seata.tcc.guide.order.api;


import io.github.weipeng2k.seata.tcc.guide.order.api.exception.OrderException;
import io.github.weipeng2k.seata.tcc.guide.order.api.param.CreateOrderParam;

/**
 * <pre>
 * 订单创建服务
 * </pre>
 *
 * @author weipeng2k 2021年08月25日 下午20:15:32
 */
public interface OrderCreateService {

    /**
     * 根据参数创建一笔订单
     *
     * @param param 订单创建参数
     * @return 返回订单主键，如果创建失败则会抛出异常
     */
    Long createOrder(CreateOrderParam param) throws OrderException;
}
