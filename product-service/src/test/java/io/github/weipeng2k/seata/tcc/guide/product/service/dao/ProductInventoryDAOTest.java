package io.github.weipeng2k.seata.tcc.guide.product.service.dao;

import io.github.weipeng2k.seata.tcc.guide.product.service.dao.impl.ProductInventoryDAOImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertSame;

/**
 * @author weipeng2k 2021年08月27日 下午15:33:34
 */
@ContextConfiguration(classes = ProductInventoryDAOTest.Config.class)
@RunWith(SpringRunner.class)
public class ProductInventoryDAOTest extends AbstractJUnit4SpringContextTests {

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(), 5, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>());
    @Autowired
    private ProductInventoryDAO productInventoryDAO;

    @AfterClass
    public static void clean() {
        threadPoolExecutor.shutdown();
    }

    @BeforeClass
    public static void init() {
        threadPoolExecutor.prestartAllCoreThreads();
    }

    @Test
    public void setPreProductInventory() {
        productInventoryDAO.setPreProductInventory(1L, 127);
        Integer preProductInventory = productInventoryDAO.getPreProductInventory(1L);
        assertSame(127, preProductInventory);
    }

    @Test
    public void setRealProductInventory() {
        productInventoryDAO.setRealProductInventory(1L, 127);
        Integer realProductInventory = productInventoryDAO.getRealProductInventory(1L);
        assertSame(127, realProductInventory);
    }

    @Test
    public void reducePreProductInventory() {
        productInventoryDAO.setPreProductInventory(2L, 200);

        CountDownLatch latch = new CountDownLatch(90);
        for (int i = 0; i < 90; i++) {
            threadPoolExecutor.execute(() -> {
                productInventoryDAO.reducePreProductInventory(2L, 2);
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            // Ignore.
        }

        Integer preProductInventory = productInventoryDAO.getPreProductInventory(2L);
        assertSame(20, preProductInventory);
    }

    @Test
    public void reduceRealProductInventory() {
        productInventoryDAO.setRealProductInventory(3L, 200);

        CountDownLatch latch = new CountDownLatch(90);
        for (int i = 0; i < 90; i++) {
            threadPoolExecutor.execute(() -> {
                productInventoryDAO.reduceRealProductInventory(3L, 2);
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            // Ignore.
        }

        Integer realProductInventory = productInventoryDAO.getRealProductInventory(3L);
        assertSame(20, realProductInventory);
    }

    @Configuration
    static class Config {
        @Bean
        ProductInventoryDAO productInventoryDAO() {
            return new ProductInventoryDAOImpl();
        }
    }
}