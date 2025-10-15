package creational.abstractfactory.rabbit;

import creational.abstractfactory.interfaces.MessageConsumer;

final class RabbitConsumer implements MessageConsumer {
    @Override public void subscribe(String topic) {
        System.out.println("Rabbit subscribed to " + topic);
    }
}