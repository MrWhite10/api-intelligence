package ir.platco.ai.documentation.agent.model;

import java.util.List;

public record SchemaDetails(

        String type,

        String format,

        List<SchemaField> fields,

        SchemaDetails items
) {
}