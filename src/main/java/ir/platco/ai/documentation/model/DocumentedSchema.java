package ir.platco.ai.documentation.model;

import java.util.List;

public record DocumentedSchema(

        String type,

        String format,

        List<DocumentedField> fields,

        DocumentedSchema items
) {
}