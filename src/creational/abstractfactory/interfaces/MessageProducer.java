package creational.abstractfactory.interfaces;

public interface MessageProducer {
    void send(String topic, String payload);
}