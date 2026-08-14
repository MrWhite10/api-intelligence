package ir.platco.ai.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import ir.platco.ai.openapi.dto.ApiEndpoint;
import ir.platco.ai.openapi.dto.OpenApiMetadata;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OpenApiParserService {

    public OpenAPI parseOpenApi(String content) {

        OpenAPI openAPI = new OpenAPIV3Parser()
                .readContents(content)
                .getOpenAPI();

        if (openAPI == null) {
            throw new IllegalArgumentException(
                    "Invalid OpenAPI specification"
            );
        }

        return openAPI;
    }

    public OpenApiMetadata extractMetadata(OpenAPI openAPI) {

        String title = null;
        String version = null;
        String description = null;

        if (openAPI.getInfo() != null) {
            title = openAPI.getInfo().getTitle();
            version = openAPI.getInfo().getVersion();
            description = openAPI.getInfo().getDescription();
        }

        List<ApiEndpoint> endpoints = new ArrayList<>();

        if (openAPI.getPaths() != null) {

            for (Map.Entry<String, PathItem> entry :
                    openAPI.getPaths().entrySet()) {

                String path = entry.getKey();
                PathItem pathItem = entry.getValue();

                addEndpoint(
                        endpoints,
                        "GET",
                        path,
                        pathItem.getGet()
                );

                addEndpoint(
                        endpoints,
                        "POST",
                        path,
                        pathItem.getPost()
                );

                addEndpoint(
                        endpoints,
                        "PUT",
                        path,
                        pathItem.getPut()
                );

                addEndpoint(
                        endpoints,
                        "DELETE",
                        path,
                        pathItem.getDelete()
                );

                addEndpoint(
                        endpoints,
                        "PATCH",
                        path,
                        pathItem.getPatch()
                );
            }
        }

        return new OpenApiMetadata(
                title,
                version,
                description,
                endpoints
        );
    }

    private void addEndpoint(
            List<ApiEndpoint> endpoints,
            String method,
            String path,
            Operation operation
    ) {

        if (operation == null) {
            return;
        }

        endpoints.add(
                new ApiEndpoint(
                        method,
                        path,
                        operation.getSummary()
                )
        );
    }
}