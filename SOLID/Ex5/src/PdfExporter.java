import java.nio.charset.StandardCharsets;

public class PdfExporter extends Exporter {
    @Override
    public ExportResult export(ExportRequest req) {
        String title = req.title == null ? "" : req.title;
        String body = req.body == null ? "" : req.body;
        String fakePdf = "PDF(" + title + "):" + body;
        return new ExportResult("application/pdf", fakePdf.getBytes(StandardCharsets.UTF_8));
    }
}
