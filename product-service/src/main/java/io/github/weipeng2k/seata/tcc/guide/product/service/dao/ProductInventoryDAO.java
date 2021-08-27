package io.github.weipeng2k.seata.tcc.guide.product.service.dao;

/**
 * <pre>
 * 商品库存持久层
 * </pre>
 *
 * @author weipeng2k 2021年08月27日 下午14:31:16
 */
public interface ProductInventoryDAO {


    /**
     * 设置预扣库存
     *
     * @param productId 商品ID
     * @param amount    库存数量
     */
    void setPreProductInventory(Long productId, Integer amount);

    /**
     * 设置真实库存
     *
     * @param productId 商品ID
     * @param amount    库存数量
     */
    void setRealProductInventory(Long productId, Integer amount);

    /**
     * 获取预扣库存
     *
     * @param productId 商品ID
     * @return 库存数量
     */
    Integer getPreProductInventory(Long productId);

    /**
     * 减少预扣库存
     *
     * @param productId 商品ID
     * @param delta     减少的量
     * @return 是否成功
     */
    boolean reducePreProductInventory(Long productId, Integer delta);

    /**
     * 获取真实库存
     *
     * @param productId 商品ID
     * @return 库存数量
     */
    Integer getRealProductInventory(Long productId);

    /**
     * 减少真实库存
     *
     * @param productId 商品ID
     * @param delta     减少的量
     * @return 是否成功
     */
    boolean reduceRealProductInventory(Long productId, Integer delta);

    /**
     * 增加预扣库存
     *
     * @param productId 商品ID
     * @param delta 增加数量
     * @return 是否成功
     */
    boolean addPreProductInventory(Long productId, Integer delta);
}
