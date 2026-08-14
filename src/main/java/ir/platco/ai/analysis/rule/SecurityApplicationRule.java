package ir.platco.ai.analysis.rule;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.dto.Severity;
import ir.platco.ai.analysis.extractor.OpenApiOperationExtractor;
import ir.platco.ai.analysis.model.ApiOperationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityApplicationRule
        implements OpenApiRule {

    private final OpenApiOperationExtractor operationExtractor;

    public SecurityApplicationRule(
            OpenApiOperationExtractor operationExtractor
    ) {
        this.operationExtractor = operationExtractor;
    }

    @Override
    public List<RuleViolation> evaluate(
            OpenAPI openAPI
    ) {

        if (!hasSecuritySchemes(openAPI)) {
            return List.of();
        }

        if (hasGlobalSecurity(openAPI)) {
            return List.of();
        }

        return operationExtractor
                .extract(openAPI)
                .stream()
                .filter(this::doesNotHaveSecurity)
                .map(context ->
                        new RuleViolation(
                                "RULE-006",
                                Severity.HIGH,
                                context.path(),
                                context.method(),
                                "A security scheme is defined but not applied to this operation."
                        )
                )
                .toList();
    }

    private boolean hasSecuritySchemes(
            OpenAPI openAPI
    ) {

        return openAPI.getComponents() != null
                && openAPI.getComponents()
                .getSecuritySchemes() != null
                && !openAPI.getComponents()
                .getSecuritySchemes()
                .isEmpty();
    }

    private boolean hasGlobalSecurity(
            OpenAPI openAPI
    ) {

        return openAPI.getSecurity() != null
                && !openAPI.getSecurity().isEmpty();
    }

    private boolean doesNotHaveSecurity(
            ApiOperationContext context
    ) {

        return context.operation()
                .getSecurity() == null
                || context.operation()
                .getSecurity()
                .isEmpty();
    }
}