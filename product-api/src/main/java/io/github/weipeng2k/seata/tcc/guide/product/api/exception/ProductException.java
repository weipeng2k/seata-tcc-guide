package io.github.weipeng2k.seata.tcc.guide.product.api.exception;

import io.github.weipeng2k.seata.tcc.guide.product.api.constants.ProductErrorCode;

/**
 * <pre>
 * 产品异常
 * </pre>
 *
 * @author weipeng2k 2021年08月26日 下午15:47:07
 */
public class ProductException extends Exception {
    private static final long serialVersionUID = 2267175296483658147L;
    /**
     * 错误码
     */
    private final String errorCode;

    public ProductException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    /**
     * <pre>
     * 获取ErrorCode {@link ProductErrorCode}
     *
     * </pre>
     *
     * @return error_code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
