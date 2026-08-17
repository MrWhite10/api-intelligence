package ir.platco.ai.documentation.application;

import ir.platco.ai.documentation.model.DocumentationResponse;

public interface GenerateDocumentationUseCase {

    DocumentationResponse generate(
            String openApiContent,
            String template
    );
}