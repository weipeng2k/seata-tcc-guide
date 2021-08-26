package io.github.weipeng2k.seata.tcc.guide.product.api;

import io.github.weipeng2k.seata.tcc.guide.product.api.exception.ProductException;
import io.github.weipeng2k.seata.tcc.guide.product.api.param.OccupyProductInventoryParam;

/**
 * <pre>
 * 商品库存服务
 * </pre>
 *
 * @author weipeng2k 2021年08月26日 下午15:39:11
 */
public interface ProductInventoryService {

    /**
     * <pre>
     * 占用某个产品的库存
     * </pre>
     *
     * @param param 占用库存参数
     * @return 占用明细主键
     * @throws ProductException 商品异常
     */
    Long occupyProductInventory(OccupyProductInventoryParam param) throws ProductException;

    /**
     * <pre>
     * 根据外部业务ID确认其占用库存
     * </pre>
     *
     * @param outBizId 外部业务ID
     * @throws ProductException 商品异常
     */
    void confirmOccupyProductInventory(Long outBizId) throws ProductException;

    /**
     * <pre>
     * 根据外部业务ID取消其占用的库存
     * </pre>
     *
     * @param outBizId 外部业务ID
     * @throws ProductException 商品异常
     */
    void cancelOccupyProductInventory(Long outBizId) throws ProductException;

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
}
