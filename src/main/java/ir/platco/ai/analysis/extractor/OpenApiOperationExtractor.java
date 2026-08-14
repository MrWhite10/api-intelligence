package ir.platco.ai.analysis.extractor;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import ir.platco.ai.analysis.model.ApiOperationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OpenApiOperationExtractor {

    public List<ApiOperationContext> extract(OpenAPI openAPI) {

        List<ApiOperationContext> operations =
                new ArrayList<>();

        if (openAPI.getPaths() == null) {
            return operations;
        }

        for (Map.Entry<String, PathItem> entry :
                openAPI.getPaths().entrySet()) {

            String path = entry.getKey();
            PathItem pathItem = entry.getValue();

            addOperation(
                    operations,
                    path,
                    "GET",
                    pathItem.getGet()
            );

            addOperation(
                    operations,
                    path,
                    "POST",
                    pathItem.getPost()
            );

            addOperation(
                    operations,
                    path,
                    "PUT",
                    pathItem.getPut()
            );

            addOperation(
                    operations,
                    path,
                    "DELETE",
                    pathItem.getDelete()
            );

            addOperation(
                    operations,
                    path,
                    "PATCH",
                    pathItem.getPatch()
            );
        }

        return operations;
    }

    private void addOperation(
            List<ApiOperationContext> operations,
            String path,
            String method,
            Operation operation) {

        if (operation == null) {
            return;
        }

        operations.add(
                new ApiOperationContext(
                        path,
                        method,
                        operation
                )
        );
    }
}