package ir.platco.ai.documentation.model;

import java.util.List;

public record DocumentationResponse(

        String statusCode,

        String description,

        List<String> contentTypes,

        DocumentationSchema schema
) {
}