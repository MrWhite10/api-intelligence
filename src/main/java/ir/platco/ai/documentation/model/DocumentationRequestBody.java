package ir.platco.ai.documentation.model;

import java.util.List;

public record DocumentationRequestBody(

        boolean required,

        List<String> contentTypes,

        DocumentationSchema schema
) {
}