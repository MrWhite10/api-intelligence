package ir.platco.ai.analysis.rule;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.analysis.dto.RuleViolation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenApiRuleEngine {

    private final List<OpenApiRule> rules;

    public OpenApiRuleEngine(List<OpenApiRule> rules) {
        this.rules = rules;
    }

    public List<RuleViolation> evaluate(OpenAPI openAPI) {

        return rules.stream()
                .flatMap(rule -> rule.evaluate(openAPI).stream())
                .toList();
    }
}