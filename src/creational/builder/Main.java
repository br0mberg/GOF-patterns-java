package creational.builder;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

final class Order {
    private final String id;          // генерируется внутри (UUID)
    private final Instant createdAt;  // ставится внутри (Instant.now())
    private final String customer;    // задаёт пользователь
    private final String comment;     // опционально

    private Order(Builder b) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.customer = Objects.requireNonNull(b.customer, "customer is required");
        this.comment = b.comment;
    }

    public static Builder builder() { return new Builder(); }

    static final class Builder {
        private String customer;
        private String comment;

        Builder customer(String v) { this.customer = v; return this; }
        Builder comment(String v)  { this.comment  = v; return this; }

        Order build() { return new Order(this); }
    }

    @Override public String toString() {
        return "Order{id='%s', createdAt=%s, customer='%s', comment='%s'}"
                .formatted(id, createdAt, customer, comment);
    }
}

public class Main {
    public static void main(String[] args) {
        Order o = Order.builder()
                .customer("andrey.brombin")
                .comment("Позвонить за час")
                .build();

        System.out.println(o);
    }
}