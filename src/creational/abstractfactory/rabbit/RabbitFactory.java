package creational.abstractfactory.rabbit;

import creational.abstractfactory.interfaces.MessageConsumer;
import creational.abstractfactory.interfaces.MessageProducer;
import creational.abstractfactory.interfaces.MessagingFactory;

public final class RabbitFactory implements MessagingFactory {
    @Override
    public MessageProducer producer() {
        return new RabbitProducer();
    }

    @Override
    public MessageConsumer consumer() {
        return new RabbitConsumer();
    }
}