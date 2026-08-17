package ir.platco.ai.documentation.model;

import java.util.List;

public record DocumentedRequestBody(

        boolean required,

        List<String> contentTypes,

        DocumentedSchema schema
) {
}