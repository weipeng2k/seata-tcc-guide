package io.github.weipeng2k.seata.tcc.guide.trade.facade;

import io.seata.core.context.RootContext;
import io.seata.core.model.BranchType;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * @author weipeng2k 2021年08月25日 下午21:44:11
 */
@SpringBootApplication
@EnableDubbo
@Configuration
public class TradeApplication implements CommandLineRunner {

    @Autowired
    private TradeAction tradeAction;

    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            RootContext.bindBranchType(BranchType.TCC);
            tradeAction.setProductInventory(1L, 5);
            RootContext.bindBranchType(BranchType.TCC);
            tradeAction.makeOrder(1L, 2L, 3);
            RootContext.bindBranchType(BranchType.TCC);
            tradeAction.makeOrder(1L, 2L, 3);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//
//    @Bean
//    GlobalTransactionScanner globalTransactionScanner() {
//        return new GlobalTransactionScanner("trade-facade", "my_test_tx_group");
//    }
}
