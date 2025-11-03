package behavioral.command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

// Получатель (Receiver): корзина
final class ShoppingCart {
    // productCode -> count
    private final Map<String, Integer> items = new LinkedHashMap<>();

    void addItem(String productCode, int count) {
        items.merge(productCode, count, Integer::sum);
    }

    void removeItem(String productCode, int count) {
        int currentCount = items.getOrDefault(productCode, 0);
        int nextCount = Math.max(0, currentCount - count);

        if (nextCount == 0) items.remove(productCode);
        else items.put(productCode, nextCount);
    }

    int countOf(String productCode) {
        return items.getOrDefault(productCode, 0);
    }
}

// Контракт команды
interface CartCommand {
    void execute();
    void undo();
}

// Конкретные команды
record AddItemToCart(ShoppingCart cart, String productCode, int count) implements CartCommand {
    @Override public void execute() {
        cart.addItem(productCode, count);
    }

    @Override public void undo() {
        cart.removeItem(productCode, count); // по учебному просто
    }
}

final class RemoveItemFromCart implements CartCommand {
    private final ShoppingCart cart;
    private final String productCode;
    private final int requestedCount;

    // Сколько реально сняли
    private int actuallyRemoved;

    RemoveItemFromCart(ShoppingCart cart, String productCode, int requestedCount) {
        this.cart = cart; this.productCode = productCode; this.requestedCount = requestedCount;
    }

    @Override public void execute() {
        int currentCount = cart.countOf(productCode);
        actuallyRemoved = Math.min(requestedCount, currentCount);
        cart.removeItem(productCode, actuallyRemoved);
    }

    @Override public void undo() {
        if (actuallyRemoved > 0) {
            cart.addItem(productCode, actuallyRemoved);
        }
    }
}

// Инициатор (Invoker): выполняет команды и хранит историю для undo
final class CommandInvoker {
    private final Deque<CartCommand> historyStack = new ArrayDeque<>();

    void run(CartCommand command) {
        command.execute();
        historyStack.push(command);
    }

    void undoLast() {
        CartCommand command = historyStack.poll();
        if (command != null) command.undo();
    }
}

// Клиент
public class Main {
    public static void main(String[] args) {
        var cart = new ShoppingCart();
        var invoker = new CommandInvoker();

        // +2 яблока
        invoker.run(new AddItemToCart(cart, "PRODUCT-APPLE", 2));
        // +1 молоко
        invoker.run(new AddItemToCart(cart, "PRODUCT-MILK", 1));
        // -1 яблоко
        invoker.run(new RemoveItemFromCart(cart, "PRODUCT-APPLE", 1));

        // вернёт 1 яблоко
        invoker.undoLast();

        // снимет всё, что было (1)
        invoker.run(new RemoveItemFromCart(cart, "PRODUCT-MILK", 5));
        // вернёт ровно 1
        invoker.undoLast();
    }
}
