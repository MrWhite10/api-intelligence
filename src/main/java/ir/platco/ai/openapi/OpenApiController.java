package ir.platco.ai.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.analysis.ApiAnalysisService;
import ir.platco.ai.analysis.dto.ApiAnalysisResponse;
import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.analysis.rule.OpenApiRuleEngine;
import ir.platco.ai.openapi.dto.OpenApiMetadata;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/openapi")
public class OpenApiController {

    private final OpenApiParserService parserService;
    private final ApiAnalysisService analysisService;
    private final OpenApiRuleEngine ruleEngine;

    public OpenApiController(
            OpenApiParserService parserService,
            ApiAnalysisService analysisService,
            OpenApiRuleEngine ruleEngine) {
        this.parserService = parserService;
        this.analysisService = analysisService;
        this.ruleEngine = ruleEngine;
    }

    @PostMapping(
            value = "/analyze",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiAnalysisResponse analyze(@RequestParam("file") MultipartFile file) throws IOException {

        String content = new String(
                file.getBytes(),
                StandardCharsets.UTF_8
        );

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