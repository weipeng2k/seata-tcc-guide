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
     * @throws OrderException 订单异常
     */
    Long createOrder(CreateOrderParam param) throws OrderException;

    /**
     * <pre>
     * 根据订单ID确认订单
     * </pre>
     *
     * @param orderId 订单ID
     * @throws OrderException 订单异常
     */
    void confirmOrder(Long orderId) throws OrderException;

    /**
     * <pre>
     * 根据订单ID作废当前订单
     * </pre>
     *
     * @param orderId 订单ID
     * @throws OrderException 订单异常
     */
    void cancelOrder(Long orderId) throws OrderException;
}
