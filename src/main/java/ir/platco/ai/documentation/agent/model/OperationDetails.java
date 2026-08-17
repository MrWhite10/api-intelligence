package ir.platco.ai.documentation.agent.model;

import java.util.List;

public record OperationDetails(
        String path,
        String method,
        String summary,
        String description,
        List<ParameterDetails> parameters,
        List<ResponseDetails> responses
) {
}