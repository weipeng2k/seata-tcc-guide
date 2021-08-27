package io.github.weipeng2k.seata.tcc.guide.product.service.dao.impl;

import io.github.weipeng2k.seata.tcc.guide.product.service.dao.ProductInventoryEntry;
import io.github.weipeng2k.seata.tcc.guide.product.service.dao.ProductInventoryEntryDAO;
import org.springframework.stereotype.Repository;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 * 单机Mock
 * </pre>
 *
 * @author weipeng2k 2021年08月27日 下午16:38:17
 */
@Repository("productInventoryEntryDAO")
public class ProductInventoryEntryDAOImpl implements ProductInventoryEntryDAO {

    // fake id generator
    private final AtomicLong idGenerator = new AtomicLong(1L);
    /**
     * 占用明细
     */
    private final ConcurrentMap<Long, ProductInventoryEntry> inventoryEntries = new ConcurrentHashMap<>();

    @Override
    public Long insertProductInventoryEntry(ProductInventoryEntry entry) {
        long id = idGenerator.getAndIncrement();
        entry.setId(id);
        ProductInventoryEntry productInventoryEntry = inventoryEntries.putIfAbsent(entry.getOutBizId(), entry);
        sleepRandomIn(100);
        return productInventoryEntry != null ? productInventoryEntry.getId() : id;
    }

    @Override
    public int updateProductInventoryEntry(Long outBizId, Integer status) {
        ProductInventoryEntry productInventoryEntry = inventoryEntries.get(outBizId);
        int rows = 0;
        if (productInventoryEntry != null) {
            productInventoryEntry.setStatus(status);
            sleepRandomIn(50);
            rows = 1;
        }

        return rows;
    }

    @Override
    public ProductInventoryEntry getProductInventoryEntry(Long outBizId) {
        sleepRandomIn(50);
        return inventoryEntries.get(outBizId);
    }

    /**
     * 随机睡眠一段时间
     *
     * @param millis 毫秒
     */
    private void sleepRandomIn(int millis) {
        try {
            Thread.sleep(new Random().nextInt(millis) + 1);
        } catch (InterruptedException e) {
            // Ignore.
        }
    }
}
