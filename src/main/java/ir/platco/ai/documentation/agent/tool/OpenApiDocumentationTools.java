package ir.platco.ai.documentation.agent.tool;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import ir.platco.ai.documentation.agent.model.OperationDetails;
import ir.platco.ai.documentation.agent.model.ParameterDetails;
import ir.platco.ai.documentation.agent.model.ResponseDetails;
import org.springframework.ai.tool.annotation.Tool;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
                    parameters, descriptions, or responses for an endpoint.
                    """
    )
    public OperationDetails getOperationDetails(
            String path,
            String method
    ) {

        System.out.println(
                "Tool called: getOperationDetails -> "
                        + method
                        + " "
                        + path
        );

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

        return new OperationDetails(
                path,
                method.toUpperCase(Locale.ROOT),
                operation.getSummary(),
                operation.getDescription(),
                extractParameters(operation),
                extractResponses(operation)
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
                                        ? parameter.getSchema().getType()
                                        : null,
                                Boolean.TRUE.equals(
                                        parameter.getRequired()
                                ),
                                parameter.getDescription()
                        )
                )
                .toList();
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
                .map(entry ->
                        new ResponseDetails(
                                entry.getKey(),
                                entry.getValue().getDescription()
                        )
                )
                .toList();
    }
}