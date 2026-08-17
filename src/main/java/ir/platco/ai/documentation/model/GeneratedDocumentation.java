package ir.platco.ai.documentation.model;

import java.util.List;

public record GeneratedDocumentation(

        String introduction,

        ApiInformation apiInformation,

        List<DocumentedOperation> operations
) {
}