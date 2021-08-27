package io.github.weipeng2k.seata.tcc.guide.product.service.dao.impl;

import io.github.weipeng2k.seata.tcc.guide.product.service.dao.ProductInventoryDAO;
import org.springframework.stereotype.Repository;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <pre>
 * 单机本地Mock
 * </pre>
 *
 * @author weipeng2k 2021年08月27日 下午15:22:59
 */
@Repository("productInventoryDAO")
public class ProductInventoryDAOImpl implements ProductInventoryDAO {
    // 预扣库存，Key为商品ID
    private final ConcurrentMap<Long, Integer> preInventories = new ConcurrentHashMap<>();
    // 预扣库存的锁，Key为商品ID
    private final ConcurrentMap<Long, Lock> preLocks = new ConcurrentHashMap<>();
    // 真实库存，Key为商品ID
    private final ConcurrentMap<Long, Integer> realInventories = new ConcurrentHashMap<>();
    // 真实库存的锁，Key为商品ID
    private final ConcurrentMap<Long, Lock> realLocks = new ConcurrentHashMap<>();

    @Override
    public void setPreProductInventory(Long productId, Integer amount) {
        preInventories.put(productId, amount);
        preLocks.putIfAbsent(productId, new ReentrantLock());
    }

    @Override
    public void setRealProductInventory(Long productId, Integer amount) {
        realInventories.put(productId, amount);
        realLocks.putIfAbsent(productId, new ReentrantLock());
    }

    @Override
    public Integer getPreProductInventory(Long productId) {
        sleepRandomIn(50);
        return preInventories.get(productId);
    }

    @Override
    public boolean reducePreProductInventory(Long productId, Integer delta) {
        Lock lock = preLocks.get(productId);
        boolean result = false;
        if (lock != null) {
            lock.lock();
            try {
                Integer current = preInventories.get(productId);

                if (current != null && current >= delta) {
                    Integer now = current - delta;
                    sleepRandomIn(200);
                    preInventories.put(productId, now);
                    result = true;
                }
            } finally {
                lock.unlock();
            }
        }

        return result;
    }

    @Override
    public Integer getRealProductInventory(Long productId) {
        sleepRandomIn(50);
        return realInventories.get(productId);
    }

    @Override
    public boolean reduceRealProductInventory(Long productId, Integer delta) {
        Lock lock = realLocks.get(productId);
        boolean result = false;
        if (lock != null) {
            lock.lock();
            try {
                Integer current = realInventories.get(productId);

                if (current != null && current >= delta) {
                    Integer now = current - delta;
                    sleepRandomIn(200);
                    realInventories.put(productId, now);
                    result = true;
                }
            } finally {
                lock.unlock();
            }
        }

        return result;
    }

    @Override
    public boolean addPreProductInventory(Long productId, Integer delta) {
        Lock lock = preLocks.get(productId);
        boolean result = false;
        if (lock != null) {
            lock.lock();
            try {
                Integer current = preInventories.get(productId);

                if (current != null) {
                    Integer now = current + delta;
                    sleepRandomIn(200);
                    preInventories.put(productId, now);
                    result = true;
                }
            } finally {
                lock.unlock();
            }
        }

        return result;
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
