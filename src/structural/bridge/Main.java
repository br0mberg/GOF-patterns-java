package structural.bridge;

import java.time.Instant;

// Структуры: получатель, каналы, события
record To(String email, String phone) {}
record Event(String type, String text) {}
record Message(String title, String text, To to, Instant ts) {}

// Implementor: канал доставки
@FunctionalInterface
interface Channel { String send(Message m); }

// Abstraction: тип уведомления; держит ссылку на Channel (МОСТ)
abstract class Notifier {
    private final Channel ch; // композиция  = "мост" между слоями

    Notifier(Channel ch) { this.ch = ch; }

    // для клиента единый контракт
    public String notify(Event e, To to) {
        return ch.send(compose(e, to)); // делегирование реализации
    }

    abstract Message compose(Event e, To to);
}

// Конкретные абстракции: меняется "что отправляем"
final class Alert extends Notifier {
    public Alert(Channel ch) { super(ch); }
    @Override protected Message compose(Event e, To to) {
        return new Message("[ALERT] " + e.type(), e.text(), to, Instant.now());
    }
}
final class Offer extends Notifier {
    public Offer(Channel ch) { super(ch); }
    @Override protected Message compose(Event e, To to) {
        return new Message("Deal", e.text(), to, Instant.now());
    }
}

// Демонстрация Bridge: свободные комбинации "что" и "как"
public class Main {
    public static void main(String[] args) {
        Channel email = m -> { System.out.println("[EMAIL] " + m.title()); return "OK"; };
        Channel sms   = m -> { System.out.println("[SMS] "   + m.text());  return "OK"; };

        var to = new To("user@example.com", "+48111222333");

        new Alert(sms).notify(new Event("DB outage", "Latency > 2s"), to);
        new Offer(email).notify(new Event("Sale", "-20%"), to);
    }
}
