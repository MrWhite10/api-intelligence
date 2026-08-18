package ir.platco.ai.documentation.renderer;

import ir.platco.ai.documentation.model.GeneratedDocumentation;

public interface DocumentationRenderer {

    String render(
            GeneratedDocumentation documentation,
            String template
    );
}