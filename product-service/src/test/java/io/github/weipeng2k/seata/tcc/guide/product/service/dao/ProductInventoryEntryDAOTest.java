package io.github.weipeng2k.seata.tcc.guide.product.service.dao;

import io.github.weipeng2k.seata.tcc.guide.product.service.dao.impl.ProductInventoryEntryDAOImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author weipeng2k 2021年08月27日 下午16:49:50
 */
@ContextConfiguration(classes = ProductInventoryEntryDAOTest.Config.class)
@RunWith(SpringRunner.class)
public class ProductInventoryEntryDAOTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private ProductInventoryEntryDAO productInventoryEntryDAO;

    @Test
    public void insertProductInventoryEntry() {
        ProductInventoryEntry entry = new ProductInventoryEntry();
        entry.setProductId(1L);
        entry.setOutBizId(3L);
        entry.setAmount(3);
        entry.setStatus(ProductInventoryEntry.INIT);
        Long aLong = productInventoryEntryDAO.insertProductInventoryEntry(entry);
        assertTrue(aLong.intValue() > 0);
    }

    @Test
    public void updateProductInventoryEntry() {
        ProductInventoryEntry entry = new ProductInventoryEntry();
        entry.setProductId(1L);
        entry.setOutBizId(3L);
        entry.setAmount(3);
        entry.setStatus(ProductInventoryEntry.INIT);
        productInventoryEntryDAO.insertProductInventoryEntry(entry);

        assertTrue(productInventoryEntryDAO.updateProductInventoryEntry(3L, ProductInventoryEntry.CANCEL) > 0);

        assertEquals(0, productInventoryEntryDAO.updateProductInventoryEntry(-1L, ProductInventoryEntry.SUCCESS));
    }

    @Configuration
    static class Config {
        @Bean
        ProductInventoryEntryDAO productInventoryEntryDAO() {
            return new ProductInventoryEntryDAOImpl();
        }
    }
}