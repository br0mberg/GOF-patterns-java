package behavioral.templatemethod;

import java.util.List;

// Шаблон (Template)
abstract class SyncJob<T> {
    // фиксируем скелет алгоритма
    public final void run() {
        lock();
        try {
            var data = fetch();              // 1) достаём данные
            var prepared = transform(data);  // 2) преобразуем (опционально)
            persist(prepared);               // 3) сохраняем
        } finally {
            unlock();
        }
    }

    // шаги по умолчанию/крючки
    protected void lock()   { System.out.println("lock"); }
    protected void unlock() { System.out.println("unlock"); }
    protected T transform(T data) { return data; } // hook — можно не переопределять

    // обязательные шаги для подклассов
    protected abstract T fetch();
    protected abstract void persist(T prepared);
}

// Конкретные варианты (подклассы меняют шаги, порядок неизменен)
final class UserSyncJob extends SyncJob<List<String>> {
    @Override protected List<String> fetch() { return List.of("ivan", "petr"); }
    @Override protected List<String> transform(List<String> d) { return d.stream().map(String::toUpperCase).toList(); }
    @Override protected void persist(List<String> users) { System.out.println("save users " + users); }
}

final class ProductSyncJob extends SyncJob<List<String>> {
    @Override protected List<String> fetch() { return List.of("tv", "phone"); }
    @Override protected void persist(List<String> items) { System.out.println("save products " + items); }
}

// Демонстрация
public class Main {
    public static void main(String[] args) {
        new UserSyncJob().run();    // lock -> fetch -> transform -> persist -> unlock
        new ProductSyncJob().run(); // тот же порядок, другие реализации шагов
    }
}