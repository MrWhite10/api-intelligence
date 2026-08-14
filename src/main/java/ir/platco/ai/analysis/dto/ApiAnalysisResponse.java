package ir.platco.ai.analysis.dto;

import java.util.List;

public record ApiAnalysisResponse(
        String apiName,
        Integer overallScore,
        Integer securityScore,
        Integer documentationScore,
        String summary,
        List<ApiIssue> issues,
        List<String> recommendations
) {
}