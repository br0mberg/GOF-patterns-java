package behavioral.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// Наблюдатель: получает ссылку на источник и тянет состояние сам (PULL)
@FunctionalInterface
interface Observer<T> { void onUpdate(Subject<T> source); }

// Источник событий
interface Subject<T> {
    void subscribe(Observer<T> o);
    void unsubscribe(Observer<T> o);
    T state(); // текущее состояние, которое наблюдатели читают в onUpdate(...)
}

// Публикатор новостей (Subject)
final class NewsPublisher implements Subject<String> {
    private final List<Observer<String>> observers = new CopyOnWriteArrayList<>();
    private volatile String lastNews; // актуальная новость

    public void publish(String news) {
        lastNews = news;
        // передаём ИСТОЧНИК, не сами данные
        observers.forEach(o -> o.onUpdate(this));
    }

    @Override public String state() { return lastNews; }
    @Override public void subscribe(Observer<String> o)   {
        observers.add(o);
    }
    @Override public void unsubscribe(Observer<String> o) {
        observers.remove(o);
    }
}

// Подписчики (Observers): тянут news через source.state()
final class EmailService implements Observer<String> {
    @Override public void onUpdate(Subject<String> source) {
        System.out.println("[EMAIL] " + source.state());
    }
}
final class SmsService implements Observer<String> {
    @Override public void onUpdate(Subject<String> source) {
        System.out.println("[SMS] " + source.state());
    }
}

// Демонстрация
public class Main {
    public static void main(String[] args) {
        var publisher = new NewsPublisher();
        var email = new EmailService();
        var sms = new SmsService();

        publisher.subscribe(email);
        publisher.subscribe(sms);

        publisher.publish("Распродажа до −50%!");
        publisher.unsubscribe(sms);
        publisher.publish("Вышла новая статья про поведенческие паттерны.");
    }
}