package ir.platco.ai.documentation.model;

import java.util.List;

public record DocumentationSchema(

        String type,

        String format,

        List<DocumentationField> fields,

        DocumentationSchema items
) {
}