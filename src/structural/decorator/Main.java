package structural.decorator;

@FunctionalInterface
interface Sender { boolean send(String text); }  // единый контракт

// База (например, HTTP/SMTP/...): просто пример
final class BaseSender implements Sender {
    @Override public boolean send(String text) {
        System.out.println("SEND -> " + text);
        return true;
    }
}

// Декоратор: логирование
final class LoggingSender implements Sender {
    private final Sender inner;
    public LoggingSender(Sender inner) { this.inner = inner; }

    @Override public boolean send(String text) {
        System.out.println("[log] start");
        boolean ok = inner.send(text);
        System.out.println("[log] done ok=" + ok);
        return ok;
    }
}

// Декоратор: ретраи (очень простой)
final class RetrySender implements Sender {
    private final Sender inner; private final int attempts;
    public RetrySender(Sender inner, int attempts) { this.inner = inner; this.attempts = attempts; }

    @Override public boolean send(String text) {
        for (int i = 1; i <= attempts; i++) {
            if (inner.send(text)) return true;
            System.out.println("[retry] attempt " + i + " failed");
        }
        return false;
    }
}

public class Main{
    public static void main(String[] args) {
        Sender base = new BaseSender();
        Sender decorated = new RetrySender(new LoggingSender(base), 3); // наслаиваем
        decorated.send("Hello, world!");
    }
}
