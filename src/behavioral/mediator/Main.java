package behavioral.mediator;

// события, о которых сообщают коллеги посреднику
enum Event { SIGNUP_SUBMITTED, USER_SAVED, SAVE_FAILED }

record User(String email) {}

// контракт посредника
interface Mediator { void notify(Event event, User user); }

// базовый класс коллег: знают только посредника
abstract class Colleague {
    protected Mediator mediator;
    void setMediator(Mediator m) { this.mediator = m; }
}

// коллеги (никаких прямых вызовов друг друга)
final class UserController extends Colleague {
    void signup(User u) {
        System.out.println("Controller: signup " + u.email());
        mediator.notify(Event.SIGNUP_SUBMITTED, u);
    }
}

final class UserRepository extends Colleague {
    void save(User u) {
        boolean ok = !u.email().endsWith("@bad");
        System.out.println(ok ? "Repo: saved " : "Repo: save FAILED");
        mediator.notify(ok ? Event.USER_SAVED : Event.SAVE_FAILED, u);
    }
}

final class Mailer extends Colleague {
    void sendWelcome(User u) {
        System.out.println("Mailer: welcome sent to " + u.email());
    }
    void sendSorry(User u) {
        System.out.println("Mailer: sorry email to " + u.email());
    }
}

// посредник: вся схема взаимодействия централизована здесь
final class SignupMediator implements Mediator {
    private final UserRepository repo;
    private final Mailer mailer;
    SignupMediator(UserController c, UserRepository r, Mailer m) {
        this.repo = r; this.mailer = m;
        c.setMediator(this); r.setMediator(this); m.setMediator(this);
    }

    @Override
    public void notify(Event e, User u) {
        switch (e) {
            case SIGNUP_SUBMITTED -> repo.save(u);
            case USER_SAVED       -> mailer.sendWelcome(u);
            case SAVE_FAILED      -> mailer.sendSorry(u);
        }
    }
}

// демо
public class Main {
    public static void main(String[] args) {
        var controller = new UserController();
        var repo = new UserRepository();
        var mailer = new Mailer();
        new SignupMediator(controller, repo, mailer);

        // saved -> welcome
        controller.signup(new User("good@example.com"));
        // failed -> sorry
        controller.signup(new User("oops@bad"));
    }
}
