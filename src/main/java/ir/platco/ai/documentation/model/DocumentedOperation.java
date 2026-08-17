package ir.platco.ai.documentation.model;

import java.util.List;

public record DocumentedOperation(

        String method,

        String path,

        String summary,

        String description,

        List<DocumentationParameter> parameters,

        DocumentationRequestBody requestBody,

        List<DocumentationResponse> responses
) {
}