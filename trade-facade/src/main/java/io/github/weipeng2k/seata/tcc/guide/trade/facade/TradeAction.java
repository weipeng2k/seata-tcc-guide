package io.github.weipeng2k.seata.tcc.guide.trade.facade;

/**
 * @author weipeng2k 2021年08月25日 下午21:38:05
 */
public interface TradeAction {
    /**
     * 商品下单接口
     *
     * @param productId 商品
     * @param buyerId   买家
     * @param amount    数量
     * @return 订单ID
     */
    Long makeOrder(Long productId, Long buyerId, Integer amount);

    /**
     * 设置库存
     *
     * @param productId 商品
     * @param amount    数量
     */
    void setProductInventory(Long productId, Integer amount);

    /**
     * 获取库存
     *
     * @param productId 商品ID
     * @return 库存
     */
    Integer getProductInventory(Long productId);
}
