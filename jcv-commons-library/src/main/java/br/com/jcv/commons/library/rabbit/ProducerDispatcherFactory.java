package br.com.jcv.commons.library.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration(value = "producerDispatcherFactory")
public class ProducerDispatcherFactory {

  @Bean(name = "producerDispatcher")
  @DependsOn("rabbitTemplate")
  public ProducerDispatcher newStreamDispatcher(
      @Qualifier("rabbitTemplate") RabbitTemplate rabbitTemplate) {
    return new ProducerDispatcher<>(rabbitTemplate, RabbitProperties.getMessageProperties());
  }
}