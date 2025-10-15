package creational.factorymethod;

// Контракт отправки уведомления
@FunctionalInterface
interface Notifier { void send(String message); }

// Фабрика/Создатель с фабричным (переопределяемым) методом
abstract class AlertService {
    // ФАБРИЧНЫЙ МЕТОД: подклассы ниже решают,
    // какой Notifier создать для выполнения alert
    protected abstract Notifier createNotifier();

    // Клиентский сценарий, который должен зависеть
    // не от конкретной реализации, а от абстракции
    public void alert(String msg) { createNotifier().send(msg); }
}

// Конкретные (не абстрактные) наследники фабрики.
// Переопределяют абстрактный фабричный метод.
// Каждый возвращает свою реализацию.
final class SmsAlerts extends AlertService {
    @Override protected Notifier createNotifier() {
        return m -> System.out.println("SMS: " + m);
    }
}

final class EmailAlerts extends AlertService {
    @Override protected Notifier createNotifier() {
        return m -> System.out.println("EMAIL: " + m);
    }
}

// Демонстрация
public class Main {
    public static void main(String[] args) {
        // выбор конкретной фабрики в зависимости от условий
        AlertService svc = (args.length > 0 && args[0].equalsIgnoreCase("sms"))
                ? new SmsAlerts()
                : new EmailAlerts();
        svc.alert("hello from Factory Method");
    }
}