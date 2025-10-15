package creational.abstractfactory.kafka;

import creational.abstractfactory.interfaces.MessageProducer;

final class KafkaProducer implements MessageProducer {
    @Override
    public void send(String topic, String payload) {
        System.out.println("Kafka send -> " + topic + ": " + payload);
    }
}