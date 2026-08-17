package ir.platco.ai.documentation.model;

public record DocumentedParameter(

        String name,

        String location,

        String type,

        boolean required,

        String description,

        String example
) {
}