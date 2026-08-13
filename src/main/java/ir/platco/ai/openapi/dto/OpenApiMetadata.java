package ir.platco.ai.openapi.dto;

import java.util.List;

public record OpenApiMetadata(
        String title,
        String version,
        String description,
        List<ApiEndpoint> endpoints
) {
}