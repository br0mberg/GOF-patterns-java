package structural.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Subject (контракт)
interface UserRepo { User find(String id); }

// Реальная реализация (дорогая/медленная, имитируем БД)
final class DbUserRepo implements UserRepo {
    private final Map<String, User> db = Map.of(
            "u1", new User("u1", "Alice"),
            "u2", new User("u2", "Bob")
    );

    @Override public User find(String id) {
        simulateLatency();
        System.out.println("[db] query " + id);
        return db.get(id);
    }

    private void simulateLatency() {
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
    }
}

// Proxy: тот же контракт, но с кешем перед делегированием
final class CachedUserRepo implements UserRepo {
    private final UserRepo inner;
    private final Map<String, User> cache = new ConcurrentHashMap<>();

    CachedUserRepo(UserRepo inner) { this.inner = inner; }

    @Override public User find(String id) {
        return cache.computeIfAbsent(id, key -> {
            System.out.println("[cache] miss " + key);
            return inner.find(key); // дорого - ходим в “реал” только один раз
        });
    }
}

// Простая модель (Java 17 record)
record User(String id, String name) {}

// Демонстрация
public class Main {
    public static void main(String[] args) {
        UserRepo repo = new CachedUserRepo(new DbUserRepo());

        System.out.println(repo.find("u1")); // промах - [db]
        System.out.println(repo.find("u1")); // попадание - из кеша, без [db]
        System.out.println(repo.find("u2")); // промах - [db]
        System.out.println(repo.find("u2")); // попадание - из кеша
    }
}
