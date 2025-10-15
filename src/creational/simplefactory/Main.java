package creational.simplefactory;

// Контракт
interface Notifier {
    void send(Contact contact, String message);
}


// Конкретные реализации контракта
final class SmsNotifier implements Notifier {
    @Override
    public void send(Contact contact, String msg){
        /* отправка в SMS API */
        System.out.println("SMS Notifier: " + contact.phone + " : " + msg);
    }
}

final class EmailNotifier implements Notifier {
    @Override
    public void send(Contact contact, String msg){
        /* отправка через SMTP */
        System.out.println("Email Notifier: Sending email to " + contact.email + ": " + msg);
    }
}

// Фабрика: инкапсулированная логика выбора реализации
final class NotifierFactory {
    static Notifier of(Channel ch) {
        return switch (ch) {
            case EMAIL -> new EmailNotifier();
            case SMS   -> new SmsNotifier();
        };
        // Для более яркой демонстрации инкапусляции логики выбора
        // Можно добавить мягкий контракт, когда сначала проверяется баланс на смс
        // И в случае нехватки средств выполняется Email рассылка
    }
}

final class Contact {
    final String email;
    final String phone;
    Contact(String email, String phone) {
        this.email = email; this.phone = phone;
    }
}

// Клиент
public class Main {
    public static void main(String[] args) {
        Contact andreyBrombin = new Contact("andrey@brombin.ru", "+123456789");

        Channel ch = (args.length > 0 && args[0].equalsIgnoreCase("sms"))
                ? Channel.SMS : Channel.EMAIL;

        // клиент работает с контрактом, а не с конкретной реализацией
        Notifier n = NotifierFactory.of(ch);
        n.send(andreyBrombin, "hello from factory method");
    }
}