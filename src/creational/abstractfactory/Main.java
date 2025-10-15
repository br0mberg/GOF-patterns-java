package creational.abstractfactory;

// Абстракция семейства
interface MessagingFactory {
    MessageProducer createProducer();   // создание отправителя
    MessageConsumer createConsumer();   // создание получателя
}

// Контракты продуктов
interface MessageProducer {
    void send(String topic, String payload); // отправка сообщения
}

interface MessageConsumer {
    void subscribe(String topic); // подписка для получения сообщений
}

// Конкретная (не абстрактная) фабрика для семейства "Kafka"
// Возвращает парные реализации продьюсера и консюмера
final class KafkaFactory implements MessagingFactory {
    @Override public MessageProducer createProducer() {
        return new KafkaProducer();
    }
    @Override public MessageConsumer createConsumer() {
        return new KafkaConsumer();
    }
}

final class RabbitFactory implements MessagingFactory {
    @Override public MessageProducer createProducer() {
        return new RabbitProducer();
    }
    @Override public MessageConsumer createConsumer() {
        return new RabbitConsumer();
    }
}

// Реализация протокола отправки сообщения семейства Kafka
final class KafkaProducer implements MessageProducer {
    @Override
    public void send(String topic, String payload) {
        System.out.println("Kafka send -> " + topic + ": " + payload);
    }
}

final class KafkaConsumer implements MessageConsumer {
    @Override
    public void subscribe(String topic) {
        System.out.println("Kafka subscribe topic -> " + topic);
    }
}

// Реализация протокола отправки сообщения семейства RabbitMQ
final class RabbitProducer implements MessageProducer {
    @Override
    public void send(String topic, String payload) {
        System.out.println("RabbitMQ send -> " + topic + ": " + payload);
    }
}

final class RabbitConsumer implements MessageConsumer {
    @Override
    public void subscribe(String topic) {
        System.out.println("RabbitMQ subscribe topic -> " + topic);
    }
}

// Клиент НЕ знает конкретных реализаций
final class NotificationService {
    private final MessageProducer producer;
    private final MessageConsumer consumer;

    // Внедряем семейство через фабрику:
    // один выбор фабрики — согласованный набор реализаций
    NotificationService(MessagingFactory f) {
        this.producer = f.createProducer();
        this.consumer = f.createConsumer();
    }

    // интерфейсы для клиента
    void listenNotify() { consumer.subscribe("alerts"); }
    void sendNotifyUser(String userId, String text) {
        producer.send("alerts", userId + ": " + text);
    }
}

// Точка входа: клиент выбирает семейство,
// а не каждую конкретную реализацию по отдельности
public class Main {
    public static void main(String[] args) {
        // Семейство по аргументу/конфигу/профилю окружения
        MessagingFactory factory = (args.length > 0 &&
                args[0].equalsIgnoreCase("rabbit"))
                ? new RabbitFactory() : new KafkaFactory();

        // Одна фабрика — согласованный комплект реализаций
        // Клиент работает через абстракции, код не зависит от Kafka/Rabbit
        NotificationService service = new NotificationService(factory);
        service.listenNotify();
        service.sendNotifyUser("1234", "Abstract Factory в деле");

       /*
        Пример вывода:
        [Kafka] subscribed to alerts
        [Kafka] send -> alerts : 1234: Abstract Factory в деле
        */
    }
}