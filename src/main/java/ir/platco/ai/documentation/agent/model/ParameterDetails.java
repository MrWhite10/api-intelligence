package ir.platco.ai.documentation.agent.model;

public record ParameterDetails(
        String name,
        String location,
        String type,
        boolean required,
        String description
) {
}