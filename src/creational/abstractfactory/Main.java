package creational.abstractfactory;

import creational.abstractfactory.interfaces.MessagingFactory;
import creational.abstractfactory.kafka.KafkaFactory;
import creational.abstractfactory.rabbit.RabbitFactory;

public class Main {
    public static void main(String[] args) {
        MessagingFactory factory = (args.length > 0 && args[0].equalsIgnoreCase("rabbit"))
                ? new RabbitFactory() : new KafkaFactory();
        
        NotificationService service = new NotificationService(factory);
        service.start();
        service.sendNotifyUser("1234", "Abstract Factory в деле");
    }
}