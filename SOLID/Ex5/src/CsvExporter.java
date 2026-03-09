import java.nio.charset.StandardCharsets;

public class CsvExporter extends Exporter {
    @Override
    public ExportResult export(ExportRequest req) {
        String body = req.body == null ? "" : req.body;
        String title = req.title == null ? "" : req.title;

        // Escape quotes and wrap if needed
        body = escapeCsvField(body);
        title = escapeCsvField(title);

        String csv = "title,body\n" + title + "," + body + "\n";

        return new ExportResult("text/csv", csv.getBytes(StandardCharsets.UTF_8));
    }

    private String escapeCsvField(String field) {
        boolean needsQuoting = field.contains(",") || field.contains("\n") || field.contains("\"");
        if (field.contains("\"")) {
            field = field.replace("\"", "\"\"");
        }
        if (needsQuoting) {
            field = "\"" + field + "\"";
        }
        return field;
    }
}
