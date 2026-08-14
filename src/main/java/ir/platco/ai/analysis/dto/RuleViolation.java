package ir.platco.ai.analysis.dto;

public record RuleViolation(
        String ruleId,
        Severity severity,
        String path,
        String method,
        String message
) {
}