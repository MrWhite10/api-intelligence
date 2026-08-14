package ir.platco.ai.analysis.rule;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.extractor.OpenApiOperationExtractor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class OpenApiRuleEngine {

    private final List<OpenApiRule> openApiRules;

    private final List<OperationRule> operationRules;

    private final OpenApiOperationExtractor operationExtractor;

    public OpenApiRuleEngine(
            List<OpenApiRule> openApiRules,
            List<OperationRule> operationRules,
            OpenApiOperationExtractor operationExtractor) {
        this.openApiRules = openApiRules;
        this.operationRules = operationRules;
        this.operationExtractor = operationExtractor;
    }

    public List<RuleViolation> evaluate(
            OpenAPI openAPI) {

        List<RuleViolation> apiViolations =
                openApiRules
                        .stream()
                        .flatMap(rule ->
                                rule.evaluate(openAPI)
                                        .stream()
                        )
                        .toList();

        List<RuleViolation> operationViolations =
                operationExtractor
                        .extract(openAPI)
                        .stream()
                        .flatMap(operation ->
                                operationRules
                                        .stream()
                                        .flatMap(rule ->
                                                rule.evaluate(operation)
                                                        .stream()
                                        )
                        )
                        .toList();

        return Stream.concat(
                        apiViolations.stream(),
                        operationViolations.stream()
                )
                .toList();
    }
}