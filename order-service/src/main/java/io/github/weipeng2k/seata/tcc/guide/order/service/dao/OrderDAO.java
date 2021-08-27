package io.github.weipeng2k.seata.tcc.guide.order.service.dao;

/**
 * <pre>
 * 订单持久层
 * </pre>
 *
 * @author weipeng2k 2021年08月27日 上午11:37:57
 */
public interface OrderDAO {

    /**
     * 插入一个订单
     *
     * @param order 订单
     * @return PK
     */
    Long insertOrder(Order order);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     */
    void cancelOrder(Long orderId);

    /**
     * 确认订单
     *
     * @param orderId 订单ID
     */
    void confirmOrder(Long orderId);

    /**
     * 根据主键查询订单
     *
     * @param orderId 订单ID
     * @return 订单
     */
    Order getOrder(Long orderId);
}
