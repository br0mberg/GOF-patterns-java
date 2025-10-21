package structural.adapter;

// Target — единый интерфейс для «семейства перевозчиков»
@FunctionalInterface
interface FreightCarrier {
    Result deliver(Cargo cargo, String from, String to);
}
enum Result { OK, RETRY, FAIL }
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
    TruckAdapter(TruckApi api) { this.api = api; }

    @Override public Result deliver(Cargo c, String from, String to) {
        try {
            return switch (api.create(c.kg(), from, to)) {
                case 0 -> Result.OK;
                case 1 -> Result.RETRY;
                default -> Result.FAIL;
            };
        } catch (TruckEx e) {
            return Result.RETRY; // сеть/временный сбой
        }
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