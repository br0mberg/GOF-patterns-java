package creational.abstractfactory.interfaces;

public interface MessagingFactory {
    MessageProducer producer();
    MessageConsumer consumer();
}


