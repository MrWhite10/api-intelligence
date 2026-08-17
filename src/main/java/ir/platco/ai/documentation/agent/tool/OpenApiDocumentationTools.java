package ir.platco.ai.documentation.agent.tool;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import ir.platco.ai.documentation.agent.model.OperationDetails;
import ir.platco.ai.documentation.agent.model.ParameterDetails;
import ir.platco.ai.documentation.agent.model.RequestBodyDetails;
import ir.platco.ai.documentation.agent.model.ResponseDetails;
import ir.platco.ai.documentation.agent.model.SchemaDetails;
import ir.platco.ai.documentation.agent.model.SchemaField;
import org.springframework.ai.tool.annotation.Tool;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OpenApiDocumentationTools {

    private final OpenAPI openAPI;

    public OpenApiDocumentationTools(
            OpenAPI openAPI
    ) {
        this.openAPI = openAPI;
    }

    @Tool(
            description = """
                    Get detailed information about a specific API operation.

                    Use this tool when you need information about
                    parameters, request bodies, response status codes,
                    or response schemas for an endpoint.
                    """
    )
    public OperationDetails getOperationDetails(
            String path,
            String method
    ) {

        PathItem pathItem =
                openAPI.getPaths()
                        .get(path);

        if (pathItem == null) {
            throw new IllegalArgumentException(
                    "Path not found: " + path
            );
        }

        Operation operation =
                getOperation(
                        pathItem,
                        method
                );

        if (operation == null) {
            throw new IllegalArgumentException(
                    "Operation not found: "
                            + method
                            + " "
                            + path
            );
        }

        List<ParameterDetails> parameters =
                extractParameters(
                        operation
                );

        RequestBodyDetails requestBody =
                extractRequestBody(
                        operation
                );

        List<ResponseDetails> responses =
                extractResponses(
                        operation
                );

        return new OperationDetails(
                method,
                path,
                operation.getSummary(),
                operation.getDescription(),
                parameters,
                requestBody,
                responses
        );
    }

    private Operation getOperation(
            PathItem pathItem,
            String method
    ) {

        return switch (
                method.toUpperCase(Locale.ROOT)
                ) {
            case "GET" -> pathItem.getGet();
            case "POST" -> pathItem.getPost();
            case "PUT" -> pathItem.getPut();
            case "PATCH" -> pathItem.getPatch();
            case "DELETE" -> pathItem.getDelete();
            case "HEAD" -> pathItem.getHead();
            case "OPTIONS" -> pathItem.getOptions();
            default -> throw new IllegalArgumentException(
                    "Unsupported HTTP method: "
                            + method
            );
        };
    }

    private List<ParameterDetails> extractParameters(
            Operation operation
    ) {

        if (operation.getParameters() == null) {
            return Collections.emptyList();
        }

        return operation.getParameters()
                .stream()
                .map(parameter ->
                        new ParameterDetails(
                                parameter.getName(),
                                parameter.getIn(),
                                parameter.getSchema() != null
                                        ? parameter.getSchema()
                                        .getType()
                                        : null,
                                Boolean.TRUE.equals(
                                        parameter.getRequired()
                                ),
                                parameter.getDescription()
                        )
                )
                .toList();
    }

    private RequestBodyDetails extractRequestBody(
            Operation operation
    ) {

        RequestBody requestBody =
                operation.getRequestBody();

        if (requestBody == null) {
            return null;
        }

        Content content =
                requestBody.getContent();

        if (content == null) {
            return new RequestBodyDetails(
                    Boolean.TRUE.equals(
                            requestBody.getRequired()
                    ),
                    Collections.emptyList(),
                    null
            );
        }

        List<String> contentTypes =
                content.keySet()
                        .stream()
                        .toList();

        SchemaDetails schema =
                content.values()
                        .stream()
                        .findFirst()
                        .map(mediaType ->
                                extractSchema(
                                        mediaType.getSchema()
                                )
                        )
                        .orElse(null);

        return new RequestBodyDetails(
                Boolean.TRUE.equals(
                        requestBody.getRequired()
                ),
                contentTypes,
                schema
        );
    }

    private List<ResponseDetails> extractResponses(
            Operation operation
    ) {

        if (operation.getResponses() == null) {
            return Collections.emptyList();
        }

        return operation.getResponses()
                .entrySet()
                .stream()
                .map(entry -> {

                    Content content =
                            entry.getValue()
                                    .getContent();

                    if (content == null) {
                        return new ResponseDetails(
                                entry.getKey(),
                                entry.getValue()
                                        .getDescription(),
                                Collections.emptyList(),
                                null
                        );
                    }

                    List<String> contentTypes =
                            content.keySet()
                                    .stream()
                                    .toList();

                    SchemaDetails schema =
                            content.values()
                                    .stream()
                                    .findFirst()
                                    .map(mediaType ->
                                            extractSchema(
                                                    mediaType.getSchema()
                                            )
                                    )
                                    .orElse(null);

                    return new ResponseDetails(
                            entry.getKey(),
                            entry.getValue()
                                    .getDescription(),
                            contentTypes,
                            schema
                    );
                })
                .toList();
    }

    private SchemaDetails extractSchema(
            Schema<?> schema
    ) {

        if (schema == null) {
            return null;
        }

        if (schema.get$ref() != null) {

            Schema<?> referencedSchema =
                    resolveSchema(
                            schema.get$ref()
                    );

            if (referencedSchema != null) {
                return extractSchema(
                        referencedSchema
                );
            }
        }

        if (schema instanceof ArraySchema arraySchema) {

            return new SchemaDetails(
                    "array",
                    null,
                    Collections.emptyList(),
                    extractSchema(
                            arraySchema.getItems()
                    )
            );
        }

        List<SchemaField> fields =
                extractSchemaFields(
                        schema
                );

        return new SchemaDetails(
                schema.getType(),
                schema.getFormat(),
                fields,
                null
        );
    }

    private List<SchemaField> extractSchemaFields(
            Schema<?> schema
    ) {

        Map<String, Schema> properties =
                schema.getProperties();

        if (properties == null) {
            return Collections.emptyList();
        }

        List<String> requiredFields =
                schema.getRequired() != null
                        ? schema.getRequired()
                        : Collections.emptyList();

        return properties.entrySet()
                .stream()
                .map(entry -> {

                    Schema<?> property =
                            entry.getValue();

                    if (property.get$ref() != null) {

                        Schema<?> referencedSchema =
                                resolveSchema(
                                        property.get$ref()
                                );

                        if (referencedSchema != null) {
                            property =
                                    referencedSchema;
                        }
                    }

                    return new SchemaField(
                            entry.getKey(),
                            property.getType(),
                            property.getFormat(),
                            requiredFields.contains(
                                    entry.getKey()
                            ),
                            property.getDescription(),
                            property.getExample() != null
                                    ? property.getExample()
                                    .toString()
                                    : null
                    );
                })
                .toList();
    }

    private Schema<?> resolveSchema(
            String reference
    ) {

        if (reference == null
                || !reference.startsWith(
                "#/components/schemas/"
        )) {
            return null;
        }

        String schemaName =
                reference.substring(
                        "#/components/schemas/"
                                .length()
                );

        if (openAPI.getComponents() == null
                || openAPI.getComponents()
                .getSchemas() == null) {
            return null;
        }

        return openAPI.getComponents()
                .getSchemas()
                .get(schemaName);
    }
}