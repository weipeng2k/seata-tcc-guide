package io.github.weipeng2k.seata.tcc.guide.product.service.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <pre>
 * 库存占用明细
 * </pre>
 *
 * @author weipeng2k 2021年08月26日 下午21:25:58
 */
@Getter
@Setter
@ToString
public class ProductInventoryEntry {

    public static final Integer INIT = 1;
    public static final Integer SUCCESS = 2;
    public static final Integer CANCEL = 4;
    /**
     * 主键
     */
    private Long id;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 外部交易ID，比如：订单
     */
    private Long outBizId;
    /**
     * 数量
     */
    private Integer amount;
    /**
     * 1表示初始化，2表示占用成功，4表示占用取消
     */
    private Integer status;

}
