package ir.platco.ai.documentation.model;

public record DocumentationField(

        String name,

        String type,

        String format,

        boolean required,

        String description,

        String example
) {
}