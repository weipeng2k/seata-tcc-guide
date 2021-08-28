package io.github.weipeng2k.seata.tcc.guide.order.service.dao;

import io.github.weipeng2k.seata.tcc.guide.order.service.dao.impl.OrderDAOImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author weipeng2k 2021年08月27日 下午18:49:53
 */
@ContextConfiguration(classes = OrderDAOTest.Config.class)
@RunWith(SpringRunner.class)
public class OrderDAOTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private OrderDAO orderDAO;

    @Test
    public void insertOrder() {
        Order order = new Order();
        order.setAmount(1);
        order.setBuyerUserId(1L);
        order.setProductId(1L);
        order.setId(1L);
        Long id = orderDAO.insertOrder(order);
        assertTrue(id.intValue() > 0);
        assertNotNull(order.getId());
    }

    @Test
    public void cancelOrder() {
    }

    @Test
    public void confirmOrder() {
    }

    @Configuration
    static class Config {
        @Bean
        OrderDAO orderDAO() {
            return new OrderDAOImpl();
        }
    }
}