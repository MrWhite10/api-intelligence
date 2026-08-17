package ir.platco.ai.documentation;

import ir.platco.ai.documentation.application.GenerateDocumentationUseCase;
import ir.platco.ai.documentation.model.GeneratedDocumentation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/documentation")
public class DocumentationController {

    private final GenerateDocumentationUseCase generateDocumentationUseCase;

    public DocumentationController(
            GenerateDocumentationUseCase generateDocumentationUseCase
    ) {
        this.generateDocumentationUseCase =
                generateDocumentationUseCase;
    }

    @PostMapping(
            value = "/generate",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public GeneratedDocumentation generate(
            @RequestParam("openapi") MultipartFile openApiFile,
            @RequestParam("template") MultipartFile templateFile
    ) throws IOException {

        String openApiContent = new String(
                openApiFile.getBytes(),
                StandardCharsets.UTF_8
        );

        String template = new String(
                templateFile.getBytes(),
                StandardCharsets.UTF_8
        );

        return generateDocumentationUseCase.generate(
                openApiContent,
                template
        );
    }
}