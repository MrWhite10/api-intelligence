package ir.platco.ai.openapi.dto;

public record ApiEndpoint(
        String method,
        String path,
        String summary
) {
}