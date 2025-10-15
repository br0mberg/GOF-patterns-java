package creational.singleton.bad;

// Имитируем "дорогую" загрузку удалённой конфигурации
final class RemoteConfig {
    RemoteConfig() {
        System.out.println("Грузим документ размером в 100 Гб" +
                " из облака со скоростью 10 Мб/сек");
        try { Thread.sleep(3000); } catch (InterruptedException e) {}; // очень долго
    }
}

final class MarketService {
    private final RemoteConfig cfg = new RemoteConfig();
}
final class BillingService {
    private final RemoteConfig cfg = new RemoteConfig();
}

public class Main {
    public static void main(String[] args) {
        new MarketService(); // Loading...
        new BillingService(); // Loading... второй раз, а зря
    }
}