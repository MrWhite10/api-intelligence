package ir.platco.ai.analysis.rule;

import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.dto.Severity;
import ir.platco.ai.analysis.model.ApiOperationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperationDescriptionRule implements OperationRule {

    @Override
    public List<RuleViolation> evaluate(ApiOperationContext context) {

        if (context.operation().getDescription() == null
                || context.operation()
                .getDescription()
                .isBlank()) {

            return List.of(
                    new RuleViolation(
                            "RULE-005",
                            Severity.LOW,
                            context.path(),
                            context.method(),
                            "Operation does not have a description."
                    )
            );
        }

        return List.of();
    }
}