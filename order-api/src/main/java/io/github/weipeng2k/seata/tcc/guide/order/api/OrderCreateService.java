package io.github.weipeng2k.seata.tcc.guide.order.api;


import io.github.weipeng2k.seata.tcc.guide.order.api.exception.OrderException;
import io.github.weipeng2k.seata.tcc.guide.order.api.param.CreateOrderParam;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * <pre>
 * 订单创建服务
 * </pre>
 *
 * @author weipeng2k 2021年08月25日 下午20:15:32
 */
@LocalTCC
public interface OrderCreateService {

    /**
     * 根据参数创建一笔订单
     *
     * @param param   订单创建参数
     * @param orderId 订单ID
     * @throws OrderException 订单异常
     */
    @TwoPhaseBusinessAction(name = "orderCreateService", commitMethod = "confirmOrder", rollbackMethod = "cancelOrder")
    void createOrder(CreateOrderParam param,
                     @BusinessActionContextParameter(paramName = "orderId") Long orderId) throws OrderException;

    /**
     * <pre>
     * 根据订单ID确认订单
     * </pre>
     *
     * @param businessActionContext 业务行为上下文
     * @throws OrderException 订单异常
     */
    void confirmOrder(BusinessActionContext businessActionContext) throws OrderException;

    /**
     * <pre>
     * 根据订单ID作废当前订单
     * </pre>
     *
     * @param businessActionContext 业务行为上下文
     * @throws OrderException 订单异常
     */
    void cancelOrder(BusinessActionContext businessActionContext) throws OrderException;
}
