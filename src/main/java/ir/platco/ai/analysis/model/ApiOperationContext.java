package ir.platco.ai.analysis.model;

import io.swagger.v3.oas.models.Operation;

public record ApiOperationContext(
        String path,
        String method,
        Operation operation) {
}