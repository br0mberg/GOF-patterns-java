package creational.abstractfactory.kafka;

import creational.abstractfactory.interfaces.MessageConsumer;

final class KafkaConsumer implements MessageConsumer {
    @Override
    public void subscribe(String topic) {
        System.out.println("Kafka subscribed to " + topic);
    }
}
