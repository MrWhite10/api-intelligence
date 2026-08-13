package ir.platco.ai.openapi;

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

    public OpenApiController(OpenApiParserService parserService) {
        this.parserService = parserService;
    }

    @PostMapping(
            value = "/parse",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public OpenApiMetadata parse(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String content = new String(
                file.getBytes(),
                StandardCharsets.UTF_8
        );

        return parserService.parse(content);
    }
}