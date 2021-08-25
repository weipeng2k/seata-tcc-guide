package io.github.weipeng2k.seata.tcc.guide.trade.facade;

/**
 * @author weipeng2k 2021年08月25日 下午21:38:05
 */
public interface TradeAction {
    /**
     * @param productId
     * @param buyerId
     * @param amount
     */
    void makeOrder(Long productId, Long buyerId, Integer amount);
}
