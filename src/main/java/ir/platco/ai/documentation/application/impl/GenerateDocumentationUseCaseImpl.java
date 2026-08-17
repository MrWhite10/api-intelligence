package ir.platco.ai.documentation.application.impl;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.documentation.DocumentationService;
import ir.platco.ai.documentation.application.GenerateDocumentationUseCase;
import ir.platco.ai.documentation.model.GeneratedDocumentation;
import ir.platco.ai.openapi.OpenApiParserService;
import ir.platco.ai.openapi.dto.OpenApiMetadata;
import org.springframework.stereotype.Service;

@Service
public class GenerateDocumentationUseCaseImpl
        implements GenerateDocumentationUseCase {

    private final OpenApiParserService parserService;

    private final DocumentationService documentationService;

    public GenerateDocumentationUseCaseImpl(
            OpenApiParserService parserService,
            DocumentationService documentationService
    ) {
        this.parserService = parserService;
        this.documentationService = documentationService;
    }

    @Override
    public GeneratedDocumentation generate(
            String openApiContent,
            String template
    ) {

        OpenAPI openAPI =
                parserService.parseOpenApi(
                        openApiContent
                );

        OpenApiMetadata metadata =
                parserService.extractMetadata(
                        openAPI
                );

        return documentationService.generate(
                openAPI,
                metadata,
                template
        );
    }
}