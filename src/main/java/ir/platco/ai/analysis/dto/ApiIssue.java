package ir.platco.ai.analysis.dto;

public record ApiIssue(
        Severity severity,
        String path,
        String method,
        String issue,
        String recommendation
) {
}