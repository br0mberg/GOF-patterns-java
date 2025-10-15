package creational.factorymethod;

// Контракт отправки уведомления
interface Notifier { void send(String message); }

// Конкретные реализации: смс'ки и электронная почта
final class SmsNotifier implements Notifier {
    @Override public void send(String msg) {
        System.out.println("SMS: " + msg);
    }
}

final class EmailNotifier implements Notifier {
    @Override public void send(String msg) {
        System.out.println("EMAIL: " + msg);
    }
}

// Фабрика/Создатель с фабричным (переопределяемым) методом
// важно обратить внимание что метод фабрики может быть дополнительной задачей, которую решает класс
abstract class AlertService {
    // ФАБРИЧНЫЙ МЕТОД: подклассы ниже решают, какой Notifier создать для выполнения alert
    abstract Notifier createNotifier();

    // Клиентский сценарий остаётся в базовом классе
    // должен зависеть не от конкретной реализации, а от абстракции
    public void alert(String msg) {
        createNotifier().send(msg);
    }
}

// Конкретные (не абстрактные) наследники фабрики.
// Переопределяют абстрактный фабричный метод.
// Каждый возвращает свою реализацию.
final class SmsAlerts extends AlertService {
    @Override Notifier createNotifier() { return new SmsNotifier(); }
}

final class EmailAlerts extends AlertService {
    @Override Notifier createNotifier() { return new EmailNotifier(); }
}

// Демонстрация
public class Main {
    public static void main(String[] args) {

        // выбор конкретной фабрики в зависимости от условий
        AlertService svc = chooseService();
        svc.alert("hello from Factory Method");
    }

    // Пример простого выбора создателя (вместо статической фабрики со switch)
    private static AlertService chooseService() {
        // Допустим баланс счета отправки смс закончился
        int smsBalance = 0;
        if (smsBalance > 10) {
            return new SmsAlerts();
        } else {
            // денег нет, отправляем email
            return new EmailAlerts();
        }
    }
}
