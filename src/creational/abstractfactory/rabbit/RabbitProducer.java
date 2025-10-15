package creational.abstractfactory.rabbit;

import creational.abstractfactory.interfaces.MessageProducer;

final class RabbitProducer implements MessageProducer {
    @Override public void send(String topic, String payload) {
        System.out.println("Rabbit send -> " + topic + ": " + payload);
    }
}