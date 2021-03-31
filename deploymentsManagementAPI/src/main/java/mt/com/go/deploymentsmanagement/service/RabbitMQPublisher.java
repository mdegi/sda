package mt.com.go.deploymentsmanagement.service;

import mt.com.go.deploymentsmanagement.config.QueueConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQPublisher {

    private QueueConfig queueConfig;

    public void setQueueConfig(QueueConfig queueConfig) {
        this.queueConfig = queueConfig;
    }

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendMessage(String messageString) {
        rabbitTemplate.convertAndSend(queueConfig.getExchange(), queueConfig.getRoutingKey(), messageString);
        System.out.println("Send msg = " + messageString);
    }
}
