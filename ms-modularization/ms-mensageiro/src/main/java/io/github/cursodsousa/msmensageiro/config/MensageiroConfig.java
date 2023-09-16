package io.github.cursodsousa.msmensageiro.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MensageiroConfig {
    @Value("${mensageiro.context.producer.exchange.direct.admin.qtde}")
    private int mensageiroContextProducerExchangeDirectAdminQtde;

    @Value("${mensageiro.context.producer.exchange.direct.marketing.qtde}")
    private int mensageiroContextProducerExchangeDirectMarketingQtde;

    @Value("${mensageiro.context.producer.exchange.direct.finance.qtde}")
    private int mensageiroContextProducerExchangeDirectFinanceQtde;

}
