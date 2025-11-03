package behavioral.visitor;

import java.util.List;

// Элементы (Element)
sealed interface DocPart permits PlainText, BoldText, Picture {
    void accept(DocVisitor v);
}
record PlainText(String text) implements DocPart {
    public void accept(DocVisitor v){ v.visit(this);}
}
// аналогично
record BoldText  (String text) implements DocPart { public void accept(DocVisitor v){ v.visit(this);} }
record Picture   (String alt ) implements DocPart { public void accept(DocVisitor v){ v.visit(this);} }

// Посетитель (Visitor)
interface DocVisitor {
    void visit(PlainText t);
    void visit(BoldText t);
    void visit(Picture p);
}

// Операция 1: HTML-рендер (элементы не меняем)
final class HtmlVisitor implements DocVisitor {
    private final StringBuilder out = new StringBuilder();
    public void visit(PlainText t){ out.append(t.text()); }
    public void visit(BoldText t) { out.append("<b>").append(t.text()).append("</b>"); }
    public void visit(Picture p)  { out.append("<img alt='").append(p.alt()).append("'/>"); }
    String result(){ return out.toString(); }
}

// Операция 2: статистика
final class StatsVisitor implements DocVisitor {
    int images, chars;
    private void add(String s){ chars += s.length(); }
    public void visit(PlainText t){ add(t.text()); }
    public void visit(BoldText t) { add(t.text()); }
    public void visit(Picture p)  { images++; }
}

// Демонстрация
public class Main {
    public static void main(String[] args) {
        List<DocPart> doc = List.of(
                new PlainText("Hello "),
                new BoldText("world"),
                new Picture("logo")
        );

        var html  = new HtmlVisitor();
        var stats = new StatsVisitor();

        doc.forEach(p -> p.accept(html));
        doc.forEach(p -> p.accept(stats));

        System.out.println(html.result()); // Hello <b>world</b><img alt='logo'/>
        System.out.println("chars=" + stats.chars + ", images=" + stats.images); // chars=10, images=1
    }
}
