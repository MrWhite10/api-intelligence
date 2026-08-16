package ir.platco.ai.openapi.application;

import ir.platco.ai.analysis.dto.ApiAnalysisResponse;

public interface AnalyzeOpenApiUseCase {

    ApiAnalysisResponse analyze(
            String content
    );

}