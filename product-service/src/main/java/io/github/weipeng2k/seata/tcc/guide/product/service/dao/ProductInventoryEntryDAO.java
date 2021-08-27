package io.github.weipeng2k.seata.tcc.guide.product.service.dao;

/**
 * <pre>
 * 商品库存持久层
 * </pre>
 *
 * @author weipeng2k 2021年08月27日 下午14:33:36
 */
public interface ProductInventoryEntryDAO {

    /**
     * 新增库存占用明细
     *
     * @param entry 占用明细
     * @return PK
     */
    Long insertProductInventoryEntry(ProductInventoryEntry entry);

    /**
     * 修改库存占用明细状态
     *
     * @param outBizId 外部BizId
     * @param status   状态
     * @return 影响行数
     */
    int updateProductInventoryEntry(Long outBizId, Integer status);

    /**
     * 根据外部BizId获取占用明细
     *
     * @param outBizId 外部BizId
     * @return 占用明细
     */
    ProductInventoryEntry getProductInventoryEntry(Long outBizId);

}
