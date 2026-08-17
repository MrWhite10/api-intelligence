package ir.platco.ai.documentation.model;

import java.util.List;

public record DocumentedResponse(

        String statusCode,

        String description,

        List<String> contentTypes,

        DocumentedSchema schema
) {
}