package ir.platco.ai.documentation.model;

public record DocumentationParameter(

        String name,

        String location,

        String type,

        boolean required,

        String description,

        String example
) {
}