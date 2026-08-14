package ir.platco.ai.analysis.rule;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.dto.Severity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OperationDescriptionRule implements OpenApiRule {

    @Override
    public List<RuleViolation> evaluate(OpenAPI openAPI) {

        List<RuleViolation> violations = new ArrayList<>();

        if (openAPI.getPaths() == null) {
            return violations;
        }

        for (Map.Entry<String, PathItem> entry :
                openAPI.getPaths().entrySet()) {

            String path = entry.getKey();
            PathItem pathItem = entry.getValue();

            checkOperation(
                    violations,
                    "GET",
                    path,
                    pathItem.getGet()
            );

            checkOperation(
                    violations,
                    "POST",
                    path,
                    pathItem.getPost()
            );

            checkOperation(
                    violations,
                    "PUT",
                    path,
                    pathItem.getPut()
            );

            checkOperation(
                    violations,
                    "DELETE",
                    path,
                    pathItem.getDelete()
            );

            checkOperation(
                    violations,
                    "PATCH",
                    path,
                    pathItem.getPatch()
            );
        }

        return violations;
    }

    private void checkOperation(
            List<RuleViolation> violations,
            String method,
            String path,
            Operation operation
    ) {

        if (operation == null) {
            return;
        }

        if (operation.getDescription() == null
                || operation.getDescription().isBlank()) {

            violations.add(
                    new RuleViolation(
                            "RULE-005",
                            Severity.LOW,
                            path,
                            method,
                            "Operation does not have a description."
                    )
            );
        }
    }
}