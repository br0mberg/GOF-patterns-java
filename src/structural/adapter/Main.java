package structural.adapter;

import java.util.Objects;

// Target — единый интерфейс для «семейства перевозчиков»
@FunctionalInterface
interface FreightCarrier {
    Result deliver(Cargo cargo, String from, String to);
}
enum Result { OK, FAIL }
record Cargo(String id, int kg) {}

// «Родной» перевозчик — поезд
final class Train implements FreightCarrier {
    @Override public Result deliver(Cargo c, String from, String to) { return Result.OK; }
}

// «Чужой» API грузовиков (менять нельзя)
final class TruckApi {
    // 0=OK, 1=NO_DRIVER, 2=BAD_ADDRESS
    int create(int kg, String from, String to) throws TruckEx {
        if (kg <= 0) throw new TruckEx("Bad weight");
        return 0;
    }
}
final class TruckEx extends Exception { TruckEx(String m){ super(m); } }

// Adapter/обёртка — делает TruckApi совместимым с FreightCarrier
final class TruckAdapter implements FreightCarrier {
    private final TruckApi api;

    // фиксируем коды чужого API внутри адаптера
    private static final int OK = 0;
    private static final int NO_DRIVER = 1;
    private static final int BAD_ADDRESS = 2;

    TruckAdapter(TruckApi api) {
        this.api = Objects.requireNonNull(api);
    }

    @Override
    public Result deliver(Cargo c, String from, String to) {
        try {
            int code = api.create(c.kg(), from, to);
            return map(code);
        } catch (TruckEx e) {
            return Result.FAIL;
        }
    }

    private static Result map(int code) {
        return switch (code) {
            case OK -> Result.OK;
            case NO_DRIVER, BAD_ADDRESS -> Result.FAIL;
            default -> Result.FAIL;
        };
    }
}

// Клиентский код — не важно, поезд или грузовик: интерфейс один
public class Main {
    public static void main(String[] args) {
        FreightCarrier train = new Train();
        FreightCarrier truck = new TruckAdapter(new TruckApi());

        var cargo = new Cargo("C-101", 500);
        train.deliver(cargo, "Склад A", "Склад B");
        truck.deliver(cargo, "Склад A", "Склад B");
    }
}