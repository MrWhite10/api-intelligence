package ir.platco.ai.documentation.renderer.markdown;

import ir.platco.ai.documentation.model.DocumentedField;
import ir.platco.ai.documentation.model.DocumentedOperation;
import ir.platco.ai.documentation.model.DocumentedParameter;
import ir.platco.ai.documentation.model.DocumentedResponse;
import ir.platco.ai.documentation.model.DocumentedSchema;
import ir.platco.ai.documentation.renderer.DocumentationRenderer;
import ir.platco.ai.documentation.model.GeneratedDocumentation;
import org.springframework.stereotype.Component;

@Component
public class MarkdownDocumentationRenderer
        implements DocumentationRenderer {

    @Override
    public String render(
            GeneratedDocumentation documentation
    ) {

        StringBuilder markdown =
                new StringBuilder();

        appendIntroduction(
                markdown,
                documentation
        );

        appendApiInformation(
                markdown,
                documentation
        );

        appendOperations(
                markdown,
                documentation
        );

        return markdown.toString();
    }

    private void appendIntroduction(
            StringBuilder markdown,
            GeneratedDocumentation documentation
    ) {

        if (documentation.introduction() == null) {
            return;
        }

        markdown.append(
                "# Introduction\n\n"
        );

        markdown.append(
                documentation.introduction()
        );

        markdown.append(
                "\n\n"
        );
    }

    private void appendApiInformation(
            StringBuilder markdown,
            GeneratedDocumentation documentation
    ) {

        if (documentation.apiInformation() == null) {
            return;
        }

        markdown.append(
                "## API Information\n\n"
        );

        markdown.append(
                "| Field | Value |\n"
        );

        markdown.append(
                "|---|---|\n"
        );

        markdown.append(
                "| Name | %s |\n"
                        .formatted(
                                valueOrEmpty(
                                        documentation
                                                .apiInformation()
                                                .name()
                                )
                        )
        );

        markdown.append(
                "| Version | %s |\n"
                        .formatted(
                                valueOrEmpty(
                                        documentation
                                                .apiInformation()
                                                .version()
                                )
                        )
        );

        markdown.append(
                "| Description | %s |\n"
                        .formatted(
                                valueOrEmpty(
                                        documentation
                                                .apiInformation()
                                                .description()
                                )
                        )
        );

        markdown.append(
                "\n"
        );
    }

    private void appendOperations(
            StringBuilder markdown,
            GeneratedDocumentation documentation
    ) {

        if (
                documentation.operations() == null
                        || documentation.operations().isEmpty()
        ) {

            return;
        }

        markdown.append(
                "## Operations\n\n"
        );

        appendOperationsSummary(
                markdown,
                documentation
        );

        documentation.operations()
                .forEach(operation ->
                        appendOperation(
                                markdown,
                                operation
                        )
                );
    }

    private void appendOperationsSummary(
            StringBuilder markdown,
            GeneratedDocumentation documentation
    ) {

        markdown.append(
                "| Method | Path | Summary |\n"
        );

        markdown.append(
                "|---|---|---|\n"
        );

        documentation.operations()
                .forEach(operation ->
                        markdown.append(
                                "| %s | %s | %s |\n"
                                        .formatted(
                                                valueOrEmpty(
                                                        operation.method()
                                                ),
                                                valueOrEmpty(
                                                        operation.path()
                                                ),
                                                valueOrEmpty(
                                                        operation.summary()
                                                )
                                        )
                        )
                );

        markdown.append(
                "\n"
        );
    }

    private void appendOperation(
            StringBuilder markdown,
            DocumentedOperation operation
    ) {

        markdown.append(
                "### %s %s\n\n"
                        .formatted(
                                valueOrEmpty(
                                        operation.method()
                                ),
                                valueOrEmpty(
                                        operation.path()
                                )
                        )
        );

        appendOperationSummary(
                markdown,
                operation
        );

        appendOperationDescription(
                markdown,
                operation
        );

        appendParameters(
                markdown,
                operation
        );

        appendRequestBody(
                markdown,
                operation
        );

        appendResponses(
                markdown,
                operation
        );
    }

    private void appendOperationSummary(
            StringBuilder markdown,
            DocumentedOperation operation
    ) {

        if (!hasText(operation.summary())) {
            return;
        }

        markdown.append(
                "**Summary:** %s\n\n"
                        .formatted(
                                operation.summary()
                        )
        );
    }

    private void appendOperationDescription(
            StringBuilder markdown,
            DocumentedOperation operation
    ) {

        if (!hasText(operation.description())) {
            return;
        }

        markdown.append(
                "**Description:** %s\n\n"
                        .formatted(
                                operation.description()
                        )
        );
    }

    private void appendParameters(
            StringBuilder markdown,
            DocumentedOperation operation
    ) {

        if (
                operation.parameters() == null
                        || operation.parameters().isEmpty()
        ) {

            return;
        }

        markdown.append(
                "#### Parameters\n\n"
        );

        markdown.append(
                "| Name | Location | Type | Required | Description | Example |\n"
        );

        markdown.append(
                "|---|---|---|---|---|---|\n"
        );

        operation.parameters()
                .forEach(parameter ->
                        appendParameter(
                                markdown,
                                parameter
                        )
                );

        markdown.append(
                "\n"
        );
    }

    private void appendParameter(
            StringBuilder markdown,
            DocumentedParameter parameter
    ) {

        markdown.append(
                "| %s | %s | %s | %s | %s | %s |\n"
                        .formatted(
                                valueOrEmpty(
                                        parameter.name()
                                ),
                                valueOrEmpty(
                                        parameter.location()
                                ),
                                valueOrEmpty(
                                        parameter.type()
                                ),
                                parameter.required()
                                        ? "Yes"
                                        : "No",
                                valueOrEmpty(
                                        parameter.description()
                                ),
                                valueOrEmpty(
                                        parameter.example()
                                )
                        )
        );
    }

    private void appendRequestBody(
            StringBuilder markdown,
            DocumentedOperation operation
    ) {

        if (operation.requestBody() == null) {
            return;
        }

        markdown.append(
                "#### Request Body\n\n"
        );

        markdown.append(
                "**Required:** %s\n\n"
                        .formatted(
                                operation
                                        .requestBody()
                                        .required()
                                        ? "Yes"
                                        : "No"
                        )
        );

        if (
                operation
                        .requestBody()
                        .contentTypes() != null
                        && !operation
                        .requestBody()
                        .contentTypes()
                        .isEmpty()
        ) {

            markdown.append(
                    "**Content Types:** %s\n\n"
                            .formatted(
                                    String.join(
                                            ", ",
                                            operation
                                                    .requestBody()
                                                    .contentTypes()
                                    )
                            )
            );
        }

        appendSchema(
                markdown,
                operation
                        .requestBody()
                        .schema()
        );
    }

    private void appendResponses(
            StringBuilder markdown,
            DocumentedOperation operation
    ) {

        if (
                operation.responses() == null
                        || operation.responses().isEmpty()
        ) {

            return;
        }

        markdown.append(
                "#### Responses\n\n"
        );

        operation.responses()
                .forEach(response ->
                        appendResponse(
                                markdown,
                                response
                        )
                );
    }

    private void appendResponse(
            StringBuilder markdown,
            DocumentedResponse response
    ) {

        markdown.append(
                "##### %s\n\n"
                        .formatted(
                                valueOrEmpty(
                                        response.statusCode()
                                )
                        )
        );

        if (response.description() != null
                && !response.description().isBlank()
        ) {

            markdown.append(
                    "**Description:** %s\n\n"
                            .formatted(
                                    response.description()
                            )
            );
        }

        if (
                response.contentTypes() != null
                        && !response.contentTypes().isEmpty()
        ) {

            markdown.append(
                    "**Content Types:** %s\n\n"
                            .formatted(
                                    String.join(
                                            ", ",
                                            response.contentTypes()
                                    )
                            )
            );
        }

        appendSchema(
                markdown,
                response.schema()
        );
    }

    private void appendSchema(
            StringBuilder markdown,
            DocumentedSchema schema
    ) {

        if (schema == null) {
            return;
        }

        markdown.append(
                "**Schema:**\n\n"
        );

        appendSchemaDetails(
                markdown,
                schema,
                0
        );

        markdown.append(
                "\n"
        );
    }

    private void appendSchemaDetails(
            StringBuilder markdown,
            DocumentedSchema schema,
            int level
    ) {

        String indent =
                "  ".repeat(
                        level
                );

        markdown.append(
                "%s- Type: %s\n"
                        .formatted(
                                indent,
                                valueOrEmpty(
                                        schema.type()
                                )
                        )
        );

        if (
                schema.format() != null
                        && !schema.format().isBlank()
        ) {

            markdown.append(
                    "%s  - Format: %s\n"
                            .formatted(
                                    indent,
                                    schema.format()
                            )
            );
        }

        if (
                schema.fields() != null
                        && !schema.fields().isEmpty()
        ) {

            markdown.append(
                    "%s  - Fields:\n"
                            .formatted(
                                    indent
                            )
            );

            schema.fields()
                    .forEach(field ->
                            appendField(
                                    markdown,
                                    field,
                                    level + 2
                            )
                    );
        }

        if (schema.items() != null) {

            markdown.append(
                    "%s  - Items:\n"
                            .formatted(
                                    indent
                            )
            );

            appendSchemaDetails(
                    markdown,
                    schema.items(),
                    level + 2
            );
        }
    }

    private void appendField(
            StringBuilder markdown,
            DocumentedField field,
            int level
    ) {

        String indent =
                "  ".repeat(
                        level
                );

        markdown.append(
                "%s- %s (%s)%s\n"
                        .formatted(
                                indent,
                                valueOrEmpty(
                                        field.name()
                                ),
                                valueOrEmpty(
                                        field.type()
                                ),
                                field.required()
                                        ? " [required]"
                                        : ""
                        )
        );

        if (field.format() != null
            && !field.format().isBlank()
        ) {

            markdown.append(
                    "%s  - Format: %s\n"
                            .formatted(
                                    indent,
                                    field.format()
                            )
            );
        }

        if (field.description() != null
                && !field.description().isBlank()
        ) {

            markdown.append(
                    "%s  - Description: %s\n"
                            .formatted(
                                    indent,
                                    field.description()
                            )
            );
        }

        if (field.example() != null
                && !field.example().isBlank()
        ) {

            markdown.append(
                    "%s  - Example: %s\n"
                            .formatted(
                                    indent,
                                    field.example()
                            )
            );
        }
    }

    private String valueOrEmpty(
            String value
    ) {

        return value != null
                && !value.isBlank()
                ? value
                : "";
    }

    private boolean hasText(String value) {
        return value != null
                && !value.isBlank();
    }
}