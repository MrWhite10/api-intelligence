package ir.platco.ai.analysis.rule;

import io.swagger.v3.oas.models.responses.ApiResponses;
import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.dto.Severity;
import ir.platco.ai.analysis.model.ApiOperationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ErrorResponseRule implements OperationRule {

    @Override
    public List<RuleViolation> evaluate(ApiOperationContext context) {

        if (context.operation().getResponses() == null) {
            return List.of();
        }

        ApiResponses responses =
                context.operation().getResponses();

        boolean hasErrorResponse = responses.keySet()
                .stream()
                .anyMatch(this::isErrorResponse);

        if (!hasErrorResponse) {

            return List.of(
                    new RuleViolation(
                            "RULE-004",
                            Severity.MEDIUM,
                            context.path(),
                            context.method(),
                            "Operation does not define any error responses."
                    )
            );
        }

        return List.of();
    }

    private boolean isErrorResponse(String responseCode) {
        return responseCode.startsWith("4")
                || responseCode.startsWith("5")
                || responseCode.equalsIgnoreCase("default");
    }
}