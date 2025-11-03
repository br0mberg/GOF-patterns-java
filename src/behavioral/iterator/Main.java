package behavioral.iterator;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

final class Playlist implements Iterable<String> {
    private static final class Node {
        final String title; Node next;
        Node(String t) { this.title = t; }
    }

    private Node head, tail;
    private int modCount; // отслеживаем изменения

    public void add(String title) {
        Objects.requireNonNull(title);
        Node n = new Node(title);
        if (head == null) head = tail = n;
        else { tail.next = n; tail = n; }
        modCount++;
    }

    @Override public Iterator<String> iterator() {
        final int expected = modCount; // снимок состояния

        return new Iterator<>() {
            private Node cur = head;

            @Override public boolean hasNext() { return cur != null; }
            @Override public String next() {
                if (expected != modCount) throw new ConcurrentModificationException();
                if (cur == null) throw new NoSuchElementException();
                String v = cur.title;
                cur = cur.next;
                return v;
            }
            @Override public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

public class Main {
    public static void main(String[] args) {
        var pl = new Playlist();
        pl.add("Intro");
        pl.add("Main Theme");
        pl.add("Finale");

        // 1) for-each использует наш Iterator
        for (var track : pl) System.out.println(track);

        // 2) Два независимых курсора
        Iterator<String> it1 = pl.iterator();
        Iterator<String> it2 = pl.iterator();
        System.out.println(it1.next()); // Intro
        System.out.println(it1.next()); // Main Theme
        System.out.println(it2.next());// Intro (другой курсор)
    }
}
