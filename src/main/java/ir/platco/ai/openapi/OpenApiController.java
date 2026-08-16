package ir.platco.ai.openapi;

import ir.platco.ai.analysis.dto.ApiAnalysisResponse;
import ir.platco.ai.openapi.application.AnalyzeOpenApiUseCase;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/openapi")
public class OpenApiController {

    private final AnalyzeOpenApiUseCase analyzeOpenApiUseCase;

    public OpenApiController(
            AnalyzeOpenApiUseCase analyzeOpenApiUseCase
    ) {
        this.analyzeOpenApiUseCase = analyzeOpenApiUseCase;
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

        return analyzeOpenApiUseCase.analyze(
                content
        );
    }
}