package io.github.weipeng2k.seata.tcc.guide.product.api;

import io.github.weipeng2k.seata.tcc.guide.product.api.exception.ProductException;
import io.github.weipeng2k.seata.tcc.guide.product.api.param.OccupyProductInventoryParam;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * <pre>
 * 商品库存服务
 * </pre>
 *
 * @author weipeng2k 2021年08月26日 下午15:39:11
 */
@LocalTCC
public interface ProductInventoryService {

    /**
     * <pre>
     * 占用某个产品的库存
     * </pre>
     *
     * @param param    占用库存参数
     * @param outBizId 外部业务ID
     * @return 占用明细主键
     * @throws ProductException 商品异常
     */
    @TwoPhaseBusinessAction(name = "productInventoryService", commitMethod = "confirmProductInventory", rollbackMethod = "cancelProductInventory")
    Long occupyProductInventory(OccupyProductInventoryParam param,
                                @BusinessActionContextParameter(paramName = "outBizId") String outBizId) throws ProductException;

    /**
     * <pre>
     * 根据外部业务ID确认其占用库存
     * </pre>
     *
     * @param businessActionContext 业务行为上下文
     * @throws ProductException 商品异常
     */
    void confirmProductInventory(BusinessActionContext businessActionContext) throws ProductException;

    /**
     * <pre>
     * 根据外部业务ID取消其占用的库存
     * </pre>
     *
     * @param businessActionContext 业务行为上下文
     * @throws ProductException 商品异常
     */
    void cancelProductInventory(BusinessActionContext businessActionContext) throws ProductException;

    /**
     * <pre>
     * 设置商品库存
     * </pre>
     *
     * @param productId 商品ID
     * @param amount    数量
     * @throws ProductException 商品异常
     */
    void setProductInventory(Long productId, Integer amount) throws ProductException;

    /**
     * <pre>
     * 获取商品的库存
     * </pre>
     *
     * @param productId 商品ID
     * @return 库存
     */
    Integer getProductInventory(Long productId);
}
