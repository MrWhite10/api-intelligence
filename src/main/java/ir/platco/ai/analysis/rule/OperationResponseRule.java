package ir.platco.ai.analysis.rule;

import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.dto.Severity;
import ir.platco.ai.analysis.model.ApiOperationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperationResponseRule implements OperationRule {

    @Override
    public List<RuleViolation> evaluate(ApiOperationContext context) {

        if (context.operation().getResponses() == null
                || context.operation()
                .getResponses()
                .isEmpty()) {

            return List.of(
                    new RuleViolation(
                            "RULE-003",
                            Severity.HIGH,
                            context.path(),
                            context.method(),
                            "Operation does not define any responses."
                    )
            );
        }

        return List.of();
    }
}