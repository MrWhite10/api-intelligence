package ir.platco.ai.documentation.agent.model;

import java.util.List;

public record RequestBodyDetails(

        boolean required,

        List<String> contentTypes,

        SchemaDetails schema
) {
}