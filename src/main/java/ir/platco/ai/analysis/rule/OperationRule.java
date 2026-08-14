package ir.platco.ai.analysis.rule;

import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.model.ApiOperationContext;

import java.util.List;

public interface OperationRule {

    List<RuleViolation> evaluate (ApiOperationContext context);

}