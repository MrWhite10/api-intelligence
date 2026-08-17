package ir.platco.ai.documentation.agent.model;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.openapi.dto.OpenApiMetadata;

public record DocumentationRequestContext(
        OpenAPI openAPI,
        OpenApiMetadata metadata,
        String template
) {
}