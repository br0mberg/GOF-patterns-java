package structural.composite;

import java.util.ArrayList;
import java.util.List;

// Component — общий контракт, наследуется блюдом и разделом
sealed interface MenuNode permits Dish, Section {
    String render(String indent); // печать дерева
    int countDishes();            // агрегирующая операция
}

// Leaf — лист — блюдо
record Dish(String name, int priceRub) implements MenuNode {
    @Override
    public String render(String indent) {
        return indent + "- " + name + " … " + priceRub + "₽";
    }
    @Override
    public int countDishes() {
        return 1;
    }
}

// Composite — раздел меню, содержит другие узлы
final class Section implements MenuNode {
    private final String name;
    private final List<MenuNode> children = new ArrayList<>();
    Section(String name) {
        this.name = name;
    }

    Section add(MenuNode... nodes) { children.addAll(List.of(nodes)); return this; }

    @Override public String render(String indent) {
        var sb = new StringBuilder(indent).append("+ ").append(name).append("\n");
        for (MenuNode n : children) sb.append(n.render(indent + "  ")).append("\n");
        return sb.toString().stripTrailing();
    }

    @Override public int countDishes() {
        return children.stream().mapToInt(MenuNode::countDishes).sum();
    }
}

// Демонстрация: работаем с деревом как с одним объектом
public class Main {
    public static void main(String[] args) {
        var pizza   = new Dish("Пицца Маргарита", 550);
        var pasta   = new Dish("Паста Карбонара", 490);
        var kvass   = new Dish("Квас", 120);
        var tea     = new Dish("Чай", 90);

        var italian = new Section("Итальянская кухня").add(pizza, pasta);
        var drinks  = new Section("Напитки").add(kvass, tea);
        var menu    = new Section("Меню").add(italian, drinks);

        System.out.println(menu.render(""));
        System.out.println("Всего блюд: " + menu.countDishes());
    }
}
