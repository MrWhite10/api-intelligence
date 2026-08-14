package ir.platco.ai.analysis.rule;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.analysis.dto.RuleViolation;

import java.util.List;

public interface OpenApiRule {

    List<RuleViolation> evaluate(OpenAPI openAPI);

}