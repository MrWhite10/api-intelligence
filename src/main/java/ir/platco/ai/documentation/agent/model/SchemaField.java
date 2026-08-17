package ir.platco.ai.documentation.agent.model;

public record SchemaField(

        String name,

        String type,

        String format,

        boolean required,

        String description,

        String example
) {
}