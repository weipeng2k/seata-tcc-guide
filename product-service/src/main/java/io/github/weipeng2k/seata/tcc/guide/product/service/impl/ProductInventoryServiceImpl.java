package io.github.weipeng2k.seata.tcc.guide.product.service.impl;

import io.github.weipeng2k.seata.tcc.guide.product.api.ProductInventoryService;
import io.github.weipeng2k.seata.tcc.guide.product.api.constants.ProductErrorCode;
import io.github.weipeng2k.seata.tcc.guide.product.api.exception.ProductException;
import io.github.weipeng2k.seata.tcc.guide.product.api.param.OccupyProductInventoryParam;
import io.github.weipeng2k.seata.tcc.guide.product.service.dao.ProductInventoryDAO;
import io.github.weipeng2k.seata.tcc.guide.product.service.dao.ProductInventoryEntry;
import io.github.weipeng2k.seata.tcc.guide.product.service.dao.ProductInventoryEntryDAO;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author weipeng2k 2021年08月26日 下午21:09:30
 */
@Service("productInventoryService")
@DubboService(interfaceClass = ProductInventoryService.class, group = "dubbo", version = "1.0.0")
public class ProductInventoryServiceImpl implements ProductInventoryService {
    @Autowired
    private ProductInventoryDAO productInventoryDAO;
    @Autowired
    private ProductInventoryEntryDAO productInventoryEntryDAO;

    @Override
    public Long occupyProductInventory(OccupyProductInventoryParam param, String outBizId) throws ProductException {
        if (param == null || param.getProductId() == null || param.getOutBizId() == null || param.getAmount() == null) {
            throw new ProductException(ProductErrorCode.ILLEGAL_ARGUMENT);
        }

        String content = String.format("外部订单{%d}预占商品{%d}库存数量为{%d}", param.getOutBizId(), param.getProductId(),
                param.getAmount());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String runtime = "@" + simpleDateFormat.format(
                new Date()) + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";

        Integer preProductInventory = productInventoryDAO.getPreProductInventory(param.getProductId());

        // 如果库存允许则预占用之
        if (preProductInventory >= param.getAmount() && productInventoryDAO.reducePreProductInventory(
                param.getProductId(), param.getAmount())) {
            ProductInventoryEntry pie = new ProductInventoryEntry();
            pie.setProductId(param.getProductId());
            pie.setAmount(param.getAmount());
            pie.setOutBizId(param.getOutBizId());
            pie.setStatus(ProductInventoryEntry.INIT);
            Long id = productInventoryEntryDAO.insertProductInventoryEntry(pie);
            String end = String.format("，预占库存成功，占用明细{%d}生成", id);
            // for eye-catching
            System.err.println(content + end + runtime);
            return id;
        } else {
            String end = "，预占库存失败";
            // for eye-catching
            System.err.println(content + end + runtime);
            throw new ProductException(ProductErrorCode.INVENTORY_NOT_ENOUGH);
        }
    }

    @Override
    public void confirmProductInventory(BusinessActionContext businessActionContext) throws ProductException {
        Object outBizIdStr = businessActionContext.getActionContext().get("outBizId");
        if (outBizIdStr != null) {
            Long outBizId = Long.parseLong(outBizIdStr.toString());
            ProductInventoryEntry pie = productInventoryEntryDAO.getProductInventoryEntry(outBizId);

            // 占用明细生效，真实库存扣减
            if (pie != null && !Objects.equals(pie.getStatus(), ProductInventoryEntry.SUCCESS)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String content = String.format("外部订单{%d}预占商品{%d}库存数量为{%d}，占用明细{%d}成功", outBizId, pie.getProductId(),
                        pie.getAmount(), pie.getId());
                String runtime = "@" + simpleDateFormat.format(new Date()) + "[" + Thread.currentThread().getName() + "] in Tx(" + businessActionContext.getXid() + ")";
                // for eye-catching
                System.err.println(content + runtime);

                productInventoryDAO.reduceRealProductInventory(pie.getProductId(), pie.getAmount());
                productInventoryEntryDAO.updateProductInventoryEntry(outBizId, ProductInventoryEntry.SUCCESS);
            }
        }
    }

    @Override
    public void cancelProductInventory(BusinessActionContext businessActionContext) throws ProductException {
        Object outBizIdStr = businessActionContext.getActionContext().get("outBizId");
        if (outBizIdStr != null) {
            Long outBizId = Long.parseLong(outBizIdStr.toString());
            ProductInventoryEntry pie = productInventoryEntryDAO.getProductInventoryEntry(outBizId);

            // 占用明细取消，预扣库存增加
            if (pie != null && !Objects.equals(pie.getStatus(), ProductInventoryEntry.CANCEL)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String begin = String.format("外部订单{%d}预占商品{%d}库存数量为{%d}，占用明细{%d}取消", outBizId, pie.getProductId(),
                        pie.getAmount(), pie.getId());
                String runtime = "@" + simpleDateFormat.format(new Date()) + "[" + Thread.currentThread().getName() + "] in Tx(" + businessActionContext.getXid() + ")";
                // for eye-catching
                System.err.println(begin + runtime);

                productInventoryDAO.addPreProductInventory(pie.getProductId(), pie.getAmount());
                productInventoryEntryDAO.updateProductInventoryEntry(outBizId, ProductInventoryEntry.CANCEL);
            }
        }
    }

    @Override
    public void setProductInventory(Long productId, Integer amount) throws ProductException {
        productInventoryDAO.setPreProductInventory(productId, amount);
        productInventoryDAO.setRealProductInventory(productId, amount);
    }

    @Override
    public Integer getProductInventory(Long productId) {
        return productInventoryDAO.getPreProductInventory(productId);
    }
}
