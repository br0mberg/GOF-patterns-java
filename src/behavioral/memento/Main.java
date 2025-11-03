package behavioral.memento;

import java.util.ArrayDeque;
import java.util.Deque;

// Originator
final class TextEditor {
    private StringBuilder buf = new StringBuilder();

    public void append(String s) { buf.append(s); }

    public void deleteLast(int n) {
        int newLen = Math.max(0, buf.length() - Math.max(0, n));
        buf.setLength(newLen);
    }

    public String text() { return buf.toString(); }

    // Сохранить/восстановить снимок (внешний мир видит только узкий интерфейс)
    public Memento save() { return new Snap(buf.toString()); }

    public void restore(Memento m) {
        this.buf = new StringBuilder(((Snap) m).state());
    }

    // Узкий тип снимка доступен снаружи (для хранения), реализация скрыта
    public sealed interface Memento permits Snap {}

    // Реализация снимка — приватная «чёрная коробка»
    private static final record Snap(String state) implements Memento {}
}

// Caretaker
public class Main {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        Deque<TextEditor.Memento> history = new ArrayDeque<>();

        editor.append("Hello");
        history.push(editor.save());              // чекпоинт #1

        editor.append(", world");
        history.push(editor.save());              // чекпоинт #2

        editor.append("!!!");
        System.out.println(editor.text());        // Hello, world!!!

        editor.restore(history.pop());
        System.out.println(editor.text());        // Hello, world

        editor.restore(history.pop());
        System.out.println(editor.text());        // Hello
    }
}