package io.github.weipeng2k.seata.tcc.guide.product.api.constants;

/**
 * <pre>
 * 商品系统错误码
 * </pre>
 *
 * @author weipeng2k 2021年08月26日 下午15:57:06
 */
public interface ProductErrorCode {
    /**
     * 参数错误
     */
    String ILLEGAL_ARGUMENT = "IllegalArgument";
    /**
     * 库存不足
     */
    String INVENTORY_NOT_ENOUGH = "InventoryNotEnough";
}
