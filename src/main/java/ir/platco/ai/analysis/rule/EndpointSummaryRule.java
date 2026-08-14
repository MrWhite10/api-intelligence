package ir.platco.ai.analysis.rule;

import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.dto.Severity;
import ir.platco.ai.analysis.model.ApiOperationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EndpointSummaryRule implements OperationRule {

    @Override
    public List<RuleViolation> evaluate(ApiOperationContext context) {

        if (context.operation().getSummary() == null
                || context.operation().getSummary().isBlank()) {

            return List.of(
                    new RuleViolation(
                            "RULE-002",
                            Severity.LOW,
                            context.path(),
                            context.method(),
                            "Endpoint does not have a summary."
                    )
            );
        }

        return List.of();
    }
}