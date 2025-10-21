package structural.flyweight;

import java.util.*;

// Легковес: неизменяемая валюта (общее состояние)
record Currency(String code) { }

// Фабрика/кэш легковесов: один объект на код валюты
final class Currencies {
    private static final Map<String, Currency> cache = new HashMap<>();
    static Currency get(String code) {
        return cache.computeIfAbsent(code, Currency::new);
    }
    static int unique() { return cache.size(); }
}

// Внешнее состояние: сумма в минорных единицах (цент/копейка) + ссылка на общий Currency
record Money(long minor, Currency cur) {
    String show() { return (minor / 100) + "." + (minor % 100) + " " + cur.code(); }
}

public class Main {
    public static void main(String[] args) {
        var usd = Currencies.get("USD"); // вернёт один и тот же объект при повторных вызовах
        var eur = Currencies.get("EUR");

        // Тысячи денег ссылаются на пару общих валют
        List<Money> list = List.of(
                new Money(1999, usd), new Money(2500, usd),
                new Money(9900, eur), new Money(1234, usd)
        );

        list.forEach(m -> System.out.println(m.show()));
        System.out.println("Уникальных Currency в кэше: " + Currencies.unique()); // 2
    }
}
