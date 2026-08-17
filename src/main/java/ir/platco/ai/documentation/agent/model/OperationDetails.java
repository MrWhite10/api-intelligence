package ir.platco.ai.documentation.agent.model;

import java.util.List;

public record OperationDetails(

        String method,

        String path,

        String summary,

        String description,

        List<ParameterDetails> parameters,

        RequestBodyDetails requestBody,

        List<ResponseDetails> responses
) {
}