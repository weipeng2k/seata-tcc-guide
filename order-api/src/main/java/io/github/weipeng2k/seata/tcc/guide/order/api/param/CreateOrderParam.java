package io.github.weipeng2k.seata.tcc.guide.order.api.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author weipeng2k 2021年08月25日 下午20:27:46
 */
@Getter
@Setter
@ToString
public class CreateOrderParam implements Serializable {
    private static final long serialVersionUID = -1648143556403399400L;

    private Long productId;

    private Long buyerUserId;

    private Integer amount;
}
