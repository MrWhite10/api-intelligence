package ir.platco.ai.documentation.application.impl;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.documentation.agent.DocumentationAgent;
import ir.platco.ai.documentation.agent.model.DocumentationRequestContext;
import ir.platco.ai.documentation.agent.tool.OpenApiDocumentationTools;
import ir.platco.ai.documentation.application.GenerateDocumentationUseCase;
import ir.platco.ai.documentation.model.GeneratedDocumentation;
import ir.platco.ai.documentation.model.DocumentationResponse;
import ir.platco.ai.documentation.renderer.DocumentationRenderer;
import ir.platco.ai.openapi.OpenApiParserService;
import ir.platco.ai.openapi.dto.OpenApiMetadata;
import org.springframework.stereotype.Service;

@Service
public class GenerateDocumentationUseCaseImpl
        implements GenerateDocumentationUseCase {

    private final OpenApiParserService parserService;
    private final DocumentationAgent documentationAgent;
    private final DocumentationRenderer documentationRenderer;

    public GenerateDocumentationUseCaseImpl(
            OpenApiParserService parserService,
            DocumentationAgent documentationAgent,
            DocumentationRenderer documentationRenderer
    ) {
        this.parserService =
                parserService;

        this.documentationAgent =
                documentationAgent;

        this.documentationRenderer =
                documentationRenderer;
    }

    @Override
    public DocumentationResponse generate(
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

        DocumentationRequestContext context =
                new DocumentationRequestContext(
                        openAPI,
                        metadata,
                        template
                );

        OpenApiDocumentationTools tools =
                new OpenApiDocumentationTools(
                        openAPI
                );

        GeneratedDocumentation documentation =
                documentationAgent.generate(
                        context,
                        tools
                );

        String content =
                documentationRenderer.render(
                        documentation
                );

        return new DocumentationResponse(
                content
        );
    }
}