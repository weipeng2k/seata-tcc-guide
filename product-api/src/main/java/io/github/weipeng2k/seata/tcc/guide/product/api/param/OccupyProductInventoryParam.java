package io.github.weipeng2k.seata.tcc.guide.product.api.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <pre>
 * 占用商品库存参数
 * </pre>
 *
 * @author weipeng2k 2021年08月26日 下午15:45:57
 */
@Getter
@Setter
@ToString
public class OccupyProductInventoryParam implements Serializable {
    private static final long serialVersionUID = -3337119135972856754L;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 数量
     */
    private Integer amount;
    /**
     * 外部业务ID，比如：订单
     */
    private Long outBizId;
}
