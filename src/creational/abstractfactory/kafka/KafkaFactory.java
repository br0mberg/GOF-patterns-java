package creational.abstractfactory.kafka;

import creational.abstractfactory.interfaces.MessageConsumer;
import creational.abstractfactory.interfaces.MessageProducer;
import creational.abstractfactory.interfaces.MessagingFactory;

public final class KafkaFactory implements MessagingFactory {
    @Override public MessageProducer producer() {
        return new KafkaProducer();
    }
    @Override public MessageConsumer consumer() {
        return new KafkaConsumer();
    }
}
