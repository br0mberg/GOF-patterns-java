package behavioral.state;

// Контекст + состояния (инкапсулированы внутри)
final class Turnstile {
    // синглтоны состояний
    static final TurnstileState LOCKED   = new Locked();
    static final TurnstileState UNLOCKED = new Unlocked();

    // текущее состояние
    private TurnstileState state = LOCKED;

    // события
    void coin(boolean paid) { state.coin(this, paid); }
    void push()             { state.push(this); }
    void timeout()          { state.timeout(this); }

    // смена состояния — доступна только внутри пакета
    void setState(TurnstileState s) { this.state = s; }

    // «эффекты»
    void lock()   { System.out.println("Turnstile: LOCKED"); }
    void unlock() { System.out.println("Turnstile: UNLOCKED"); }
    void alarm()  { System.out.println("Turnstile: ALARM!"); }
    void thank()  { System.out.println("Turnstile: THANK YOU"); }
}

sealed interface TurnstileState permits Locked, Unlocked {
    void coin(Turnstile t, boolean paid);
    void push(Turnstile t);
    default void timeout(Turnstile t) {} // по умолчанию — нет реакции
}

final class Locked implements TurnstileState {
    @Override public void coin(Turnstile t, boolean paid) {
        if (paid) { t.unlock(); t.setState(Turnstile.UNLOCKED); }
        else t.alarm();
    }
    @Override public void push(Turnstile t) { t.alarm(); }
}

final class Unlocked implements TurnstileState {
    @Override public void coin(Turnstile t, boolean paid) {
        t.thank(); // заплатил ещё раз? поблагодарим :)
    }
    @Override public void push(Turnstile t) {
        t.lock();
        t.setState(Turnstile.LOCKED);
    }
    @Override public void timeout(Turnstile t) {
        t.lock();
        t.setState(Turnstile.LOCKED);
    }
}

// Демонстрация
public class Main {
    public static void main(String[] args) {
        var gate = new Turnstile();

        gate.push(); // ALARM! (закрыт)
        gate.coin(false); // ALARM! (денег не хватило)
        gate.coin(true); // UNLOCKED
        gate.coin(true); // THANK YOU (лишняя оплата)
        gate.timeout(); // LOCKED (автозакрытие)
        gate.coin(true);  // UNLOCKED
        gate.push(); // LOCKED (прошёл)
    }
}