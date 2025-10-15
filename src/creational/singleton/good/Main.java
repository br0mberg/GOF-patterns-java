package creational.singleton.good;

final class RemoteConfig {
    // приватный конструктор
    private RemoteConfig() {
        System.out.println("Грузим документ размером в 100 Гб" +
                " из облака со скоростью 10 Мб/сек");
        try { Thread.sleep(3000); } catch (InterruptedException e) {} // очень долго
    }

    private static class Holder {
        static final RemoteConfig I = new RemoteConfig();
    }

    // глобальная точка доступа
    public static RemoteConfig get() { return Holder.I; }
}

final class MarketService {
    private final RemoteConfig cfg = RemoteConfig.get();
}
final class BillingService {
    private final RemoteConfig cfg = RemoteConfig.get();
}

public class Main {
    public static void main(String[] args) {
        new MarketService(); // Loading...
        new BillingService(); // чиллаут.
    }
}