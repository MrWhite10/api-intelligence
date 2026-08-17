package ir.platco.ai.documentation.model;

import java.util.List;

public record DocumentedOperation(

        String method,

        String path,

        String summary,

        String description,

        List<DocumentedParameter> parameters,

        DocumentedRequestBody requestBody,

        List<DocumentedResponse> responses
) {
}