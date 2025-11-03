package behavioral.cor;

import java.util.List;

sealed interface Result permits Result.Handled, Result.Unhandled {
    record Handled(String message) implements Result {}
    final class Unhandled implements Result {}
}

record Ticket(String subject, Severity severity, boolean inFAQ) {}
enum Severity { LOW, MID, HIGH, CRITICAL }

interface Handler { Result handle(Ticket t); }

abstract class AbstractHandler implements Handler {
    private final Handler next;
    protected AbstractHandler(Handler next) { this.next = next; }
    protected Result forward(Ticket t) { return next == null ? new Result.Unhandled() : next.handle(t); }
}

// 1) Бот: отвечает по FAQ
final class Bot extends AbstractHandler {
    Bot(Handler next) { super(next); }
    public Result handle(Ticket t) {
        return t.inFAQ() ? new Result.Handled("Бот: ответил по FAQ") : forward(t);
    }
}

// 2) L1: берёт простые
final class L1 extends AbstractHandler {
    L1(Handler next) { super(next); }
    public Result handle(Ticket t) {
        return (t.severity().ordinal() <= Severity.MID.ordinal())
                ? new Result.Handled("L1: решено")
                : forward(t);
    }
}

// 3) R&D: последний (можно убрать, тогда допустим Unhandled)
final class RnD extends AbstractHandler {
    RnD() { super(null); }
    public Result handle(Ticket t) { return new Result.Handled("Эскалация в R&D: взяли в работу"); }
}

public class Main {
    public static void main(String[] args) {
        Handler chain = new Bot(new L1(new RnD()));

        for (var t : List.of(
                new Ticket("Как сменить пароль?", Severity.LOW, true),
                new Ticket("Смена тарифа", Severity.MID, false),
                new Ticket("Падает прод", Severity.CRITICAL, false))) {

            var r = chain.handle(t);
            switch (r) {
                case Result.Handled h -> System.out.println(h.message());
                case Result.Unhandled u -> System.out.println("Некому обработать");
            }
        }
    }
}
