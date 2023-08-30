package br.com.jcv.commons.library.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerDispatcher<T extends IPublisher> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RabbitTemplate rabbitTemplate;
    private final MessageProperties messageProperties;

    private final AtomicInteger myInt = new AtomicInteger();

    public ProducerDispatcher(RabbitTemplate rabbitTemplate, MessageProperties messageProperties) {

        this.rabbitTemplate = rabbitTemplate;
        this.messageProperties = messageProperties;
    }

    public void dispatch(T inputObject, boolean useDeadLetter) {

        dispatch(inputObject, useDeadLetter, new MessageProperties());
    }

    public void dispatch(T inputObject, boolean useDeadLetter, MessageProperties mproperties) {
        logger.info("Starting method dispatch");
        String json = GsonFactory.getInstance().toJson(inputObject);

        logger.info("Routing key name: {}, json:{}", inputObject.getRoutingKey(), json);

        mproperties.setContentType("application/json");

        if (useDeadLetter)
            rabbitTemplate.send(inputObject.getDeadLetterRoutingKey(), new Message(json.getBytes(), mproperties));
        else
            rabbitTemplate.send(inputObject.getRoutingKey(), new Message(json.getBytes(StandardCharsets.UTF_8), mproperties));

        logger.info("Message Sent Routing key name: {}, json:{}", inputObject.getRoutingKey(), json);

        myInt.getAndIncrement();

        if (myInt.get() % 1000 == 0)
            logger.info("Sent {} entities {} so far", myInt.get(), inputObject.getClass().getSimpleName());
    }
}