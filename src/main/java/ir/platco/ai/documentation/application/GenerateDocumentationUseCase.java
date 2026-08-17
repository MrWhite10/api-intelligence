package ir.platco.ai.documentation.application;

import ir.platco.ai.documentation.model.GeneratedDocumentation;

public interface GenerateDocumentationUseCase {

    GeneratedDocumentation generate(
            String openApiContent,
            String template
    );

}