package ir.platco.ai.openapi;

import ir.platco.ai.analysis.ApiAnalysisService;
import ir.platco.ai.analysis.dto.ApiAnalysisResponse;
import ir.platco.ai.openapi.dto.OpenApiMetadata;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/openapi")
public class OpenApiController {

    private final OpenApiParserService parserService;
    private final ApiAnalysisService analysisService;

    public OpenApiController(
            OpenApiParserService parserService,
            ApiAnalysisService analysisService
    ) {
        this.parserService = parserService;
        this.analysisService = analysisService;
    }

    @PostMapping(
            value = "/analyze",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiAnalysisResponse analyze(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String content = new String(
                file.getBytes(),
                StandardCharsets.UTF_8
        );

        OpenApiMetadata metadata =
                parserService.parse(content);

        return analysisService.analyze(metadata);
    }
}