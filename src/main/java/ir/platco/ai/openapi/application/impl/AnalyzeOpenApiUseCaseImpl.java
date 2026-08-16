package ir.platco.ai.openapi.application.impl;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.analysis.ApiAnalysisService;
import ir.platco.ai.analysis.dto.ApiAnalysisResponse;
import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.rule.OpenApiRuleEngine;
import ir.platco.ai.openapi.OpenApiParserService;
import ir.platco.ai.openapi.application.AnalyzeOpenApiUseCase;
import ir.platco.ai.openapi.dto.OpenApiMetadata;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyzeOpenApiUseCaseImpl
        implements AnalyzeOpenApiUseCase {

    private final OpenApiParserService parserService;

    private final ApiAnalysisService analysisService;

    private final OpenApiRuleEngine ruleEngine;

    public AnalyzeOpenApiUseCaseImpl(
            OpenApiParserService parserService,
            ApiAnalysisService analysisService,
            OpenApiRuleEngine ruleEngine
    ) {
        this.parserService = parserService;
        this.analysisService = analysisService;
        this.ruleEngine = ruleEngine;
    }

    @Override
    public ApiAnalysisResponse analyze(String content) {

        OpenAPI openAPI =
                parserService.parseOpenApi(content);

        OpenApiMetadata metadata =
                parserService.extractMetadata(openAPI);

        List<RuleViolation> violations =
                ruleEngine.evaluate(openAPI);

        return analysisService.analyze(
                metadata,
                violations
        );
    }
}