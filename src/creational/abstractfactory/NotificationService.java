package creational.abstractfactory;

import creational.abstractfactory.interfaces.MessageConsumer;
import creational.abstractfactory.interfaces.MessageProducer;
import creational.abstractfactory.interfaces.MessagingFactory;

final class NotificationService {
    private final MessageProducer producer;
    private final MessageConsumer consumer;

    NotificationService(MessagingFactory f) {
        this.producer = f.producer();
        this.consumer = f.consumer();
    }

    void start() {
        consumer.subscribe("alerts");
    }

    void sendNotifyUser(String userId, String text) {
        producer.send("alerts", userId + ": " + text);
    }
}