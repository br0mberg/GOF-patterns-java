package structural.facade;

// Доменные объекты
record Draft(String templateId, String payload, String authorId, String approverId) {}
record CreateResult(String docId) {}

// Контракты подсистем
@FunctionalInterface interface TemplateEngine { String render(String templateId, String payload); }
@FunctionalInterface interface Signer         { String sign(String content); }
@FunctionalInterface interface Repository     { String save(String content, String signature); } // -> new docId

// Facade — один простой метод поверх нескольких сервисов
final class DocumentFacade {
    private final TemplateEngine tpl;
    private final Signer signer;
    private final Repository repo;

    DocumentFacade(TemplateEngine tpl, Signer signer, Repository repo) {
        this.tpl = tpl; this.signer = signer; this.repo = repo;
    }

    public CreateResult create(Draft d) {
        String content   = tpl.render(d.templateId(), d.payload());
        String signature = signer.sign(content);
        String docId     = repo.save(content, signature);
        return new CreateResult(docId);
    }
}

// Демонстрация: клиенту доступен один метод create(...)
public class Main {
    public static void main(String[] args) {
        // Построение фасада за ширмой
        TemplateEngine tpl = (id, body) -> "DOC[" + id + "]\n" + body;
        Signer signer      = content -> "sig-" + Integer.toHexString(content.hashCode());
        Repository repo    = (content, sig) -> "doc-" + Math.abs((content + sig).hashCode());
        var facade = new DocumentFacade(tpl, signer, repo);

        // использование на клиенте
        var result = facade.create(new Draft("TPL-001", "Счёт на оплату #42", "user-1", "boss-9"));

        System.out.println("New docId=" + result.docId());
    }
}
