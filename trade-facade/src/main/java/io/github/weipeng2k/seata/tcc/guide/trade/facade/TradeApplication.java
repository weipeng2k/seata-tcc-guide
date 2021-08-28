package io.github.weipeng2k.seata.tcc.guide.trade.facade;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author weipeng2k 2021年08月25日 下午21:44:11
 */
@SpringBootApplication
@EnableDubbo
@Configuration
public class TradeApplication implements CommandLineRunner {

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());
    @Autowired
    private TradeAction tradeAction;

    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        tradeAction.setProductInventory(1L, 20);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch stop = new CountDownLatch(10);
        AtomicInteger orderCount = new AtomicInteger();
        for (int i = 1; i <= 10; i++) {
            int userId = i;
            threadPoolExecutor.execute(() -> {
                try {
                    start.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    tradeAction.makeOrder(1L, (long) userId, 3);
                    orderCount.incrementAndGet();
                } catch (Exception ex) {
                    // Ignore.
                } finally {
                    stop.countDown();
                }
            });
        }

        start.countDown();

        stop.await();

        Thread.sleep(1000);

        System.err.println("订单数量：" + orderCount.get());
        System.err.println("库存余量：" + tradeAction.getProductInventory(1L));
    }
}
