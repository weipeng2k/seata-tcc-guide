package io.github.weipeng2k.seata.tcc.guide.order.service.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author weipeng2k 2021年08月26日 下午22:08:24
 */
@Getter
@Setter
@ToString
public class Order {
    /**
     * ID
     */
    private Long id;
    /**
     * 买家ID
     */
    private Long buyerUserId;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 数量
     */
    private Integer amount;
    /**
     * 是否可用
     */
    private boolean enable;
}
