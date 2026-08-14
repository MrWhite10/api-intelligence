package ir.platco.ai.analysis.rule;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.dto.Severity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecuritySchemeRule implements OpenApiRule {

    @Override
    public List<RuleViolation> evaluate(OpenAPI openAPI) {

        boolean hasSecuritySchemes =
                openAPI.getComponents() != null
                        && openAPI.getComponents()
                        .getSecuritySchemes() != null
                        && !openAPI.getComponents()
                        .getSecuritySchemes()
                        .isEmpty();

        if (hasSecuritySchemes) {
            return List.of();
        }

        return List.of(
                new RuleViolation(
                        "RULE-001",
                        Severity.HIGH,
                        null,
                        null,
                        "No security scheme is defined in the OpenAPI specification."
                )
        );
    }
}