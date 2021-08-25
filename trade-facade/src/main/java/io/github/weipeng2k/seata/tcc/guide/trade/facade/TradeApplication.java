package io.github.weipeng2k.seata.tcc.guide.trade.facade;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author weipeng2k 2021年08月25日 下午21:44:11
 */
@SpringBootApplication
@EnableDubbo
public class TradeApplication implements CommandLineRunner {

    @Autowired
    private TradeAction tradeAction;

    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        tradeAction.makeOrder(1L, 2L, 3);
    }
}
