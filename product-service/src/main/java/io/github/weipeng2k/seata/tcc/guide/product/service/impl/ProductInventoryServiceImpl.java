package io.github.weipeng2k.seata.tcc.guide.product.service.impl;

import io.github.weipeng2k.seata.tcc.guide.product.api.ProductInventoryService;
import io.github.weipeng2k.seata.tcc.guide.product.api.constants.ProductErrorCode;
import io.github.weipeng2k.seata.tcc.guide.product.api.exception.ProductException;
import io.github.weipeng2k.seata.tcc.guide.product.api.param.OccupyProductInventoryParam;
import io.seata.core.context.RootContext;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author weipeng2k 2021年08月26日 下午21:09:30
 */
@Service("productInventoryService")
@DubboService(interfaceClass = ProductInventoryService.class, group = "dubbo", version = "1.0.0")
public class ProductInventoryServiceImpl implements ProductInventoryService {
    /**
     * 预扣库存
     */
    private ConcurrentMap<Long, AtomicInteger> preProductInventories = new ConcurrentHashMap<>();
    /**
     * 实际库存
     */
    private ConcurrentMap<Long, AtomicInteger> realProductInventories = new ConcurrentHashMap<>();
    /**
     * 占用明细
     */
    private ConcurrentMap<Long, OccupyProductInventoryEntry> occupyProductInventoryEntries = new ConcurrentHashMap<>();
    // fake id generator
    private AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Long occupyProductInventory(OccupyProductInventoryParam param) throws ProductException {
        if (param == null || param.getProductId() == null || param.getOutBizId() == null || param.getAmount() == null) {
            throw new ProductException(ProductErrorCode.ILLEGAL_ARGUMENT);
        }

        String begin = String.format("外部订单{%d}预占商品{%d}库存数量为{%d}", param.getOutBizId(), param.getProductId(),
                param.getAmount());
        String runtime = "@" + new Date() + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";
        System.out.println(begin + runtime);

        try {
            Thread.sleep(new Random().nextInt(200));
        } catch (InterruptedException e) {
            // Ignore.
        }

        AtomicInteger preInventory = preProductInventories.get(param.getProductId());
        // 如果库存允许则预占用之
        if (preInventory.get() >= param.getAmount()) {
            long id = idGenerator.getAndIncrement();
            OccupyProductInventoryEntry opie = new OccupyProductInventoryEntry();
            opie.setId(id);
            opie.setProductId(param.getProductId());
            opie.setAmount(param.getAmount());
            opie.setStatus(OccupyProductInventoryEntry.INIT);
            occupyProductInventoryEntries.put(param.getOutBizId(), opie);
            String end = String.format("，占用明细{%d}生成", id);
            System.out.println(begin + end + runtime);
            return id;
        } else {
            String end = "，预占库存失败";
            System.out.println(begin + end + runtime);
            throw new ProductException(ProductErrorCode.INVENTORY_NOT_ENOUGH);
        }
    }

    @Override
    public void confirmOccupyProductInventory(Long outBizId) throws ProductException {
        if (outBizId == null) {
            throw new ProductException(ProductErrorCode.ILLEGAL_ARGUMENT);
        }

        OccupyProductInventoryEntry occupyProductInventoryEntry = occupyProductInventoryEntries.get(outBizId);

        // 占用明细生效，真实库存扣减
        if (occupyProductInventoryEntry != null) {
            try {
                Thread.sleep(new Random().nextInt(200));
            } catch (InterruptedException e) {
                // Ignore.
            }

            String begin = String.format("外部订单{%d}预占商品{%d}库存数量为{%d}，占用明细{%d}成功", outBizId, occupyProductInventoryEntry.getProductId(),
                    occupyProductInventoryEntry.getAmount(), occupyProductInventoryEntry.getId());
            String runtime = "@" + new Date() + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";
            System.out.println(begin + runtime);

            occupyProductInventoryEntry.setStatus(OccupyProductInventoryEntry.SUCCESS);
            AtomicInteger realInventory = realProductInventories.get(occupyProductInventoryEntry.getProductId());
            realInventory.addAndGet(-occupyProductInventoryEntry.getAmount());
        }
    }

    @Override
    public void cancelOccupyProductInventory(Long outBizId) throws ProductException {
        if (outBizId == null) {
            throw new ProductException(ProductErrorCode.ILLEGAL_ARGUMENT);
        }

        OccupyProductInventoryEntry occupyProductInventoryEntry = occupyProductInventoryEntries.get(outBizId);
        // 占用明细取消，预扣库存增加
        if (occupyProductInventoryEntry != null) {
            try {
                Thread.sleep(new Random().nextInt(200));
            } catch (InterruptedException e) {
                // Ignore.
            }

            String begin = String.format("外部订单{%d}预占商品{%d}库存数量为{%d}，占用明细{%d}取消", outBizId, occupyProductInventoryEntry.getProductId(),
                    occupyProductInventoryEntry.getAmount(), occupyProductInventoryEntry.getId());
            String runtime = "@" + new Date() + "[" + Thread.currentThread().getName() + "] in Tx(" + RootContext.getXID() + ")";
            System.out.println(begin + runtime);

            occupyProductInventoryEntry.setStatus(OccupyProductInventoryEntry.CANCEL);
            AtomicInteger preInventory = preProductInventories.get(occupyProductInventoryEntry.getProductId());
            preInventory.addAndGet(occupyProductInventoryEntry.getAmount());
        }
    }

    @Override
    public void setProductInventory(Long productId, Integer amount) throws ProductException {
        preProductInventories.computeIfAbsent(productId, pId -> new AtomicInteger(amount));
        realProductInventories.computeIfAbsent(productId, pId -> new AtomicInteger(amount));
    }
}
