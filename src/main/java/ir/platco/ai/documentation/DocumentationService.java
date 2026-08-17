package ir.platco.ai.documentation;

import ir.platco.ai.documentation.model.GeneratedDocumentation;
import ir.platco.ai.openapi.dto.OpenApiMetadata;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class DocumentationService {

    private final ChatClient chatClient;

    public DocumentationService(
            ChatClient.Builder chatClientBuilder
    ) {
        this.chatClient = chatClientBuilder.build();
    }

    public GeneratedDocumentation generate(
            OpenApiMetadata metadata,
            String template
    ) {

        String endpointsContext =
                buildEndpointsContext(metadata);

        String content = chatClient
                .prompt()
                .system("""
                        You are an expert technical documentation writer.

                        Your task is to generate API documentation based only on
                        the provided OpenAPI information.

                        Follow the provided documentation template exactly.

                        Rules:
                        - Do not invent API endpoints.
                        - Do not invent request parameters.
                        - Do not invent response fields.
                        - If information is missing, clearly state that it is
                          not defined in the OpenAPI specification.
                        - Preserve the structure and language of the template.
                        - Return only the final Markdown documentation.
                        """)
                .user("""
                        Documentation Template:

                        %s

                        API Metadata:

                        Name: %s
                        Version: %s
                        Description: %s

                        Endpoints:

                        %s
                        """.formatted(
                        template,
                        metadata.title(),
                        metadata.version(),
                        metadata.description(),
                        endpointsContext
                ))
                .call()
                .content();

        return new GeneratedDocumentation(
                content
        );
    }

    private String buildEndpointsContext(
            OpenApiMetadata metadata
    ) {

        return metadata.endpoints()
                .stream()
                .map(endpoint ->
                        """
                        Method: %s
                        Path: %s
                        Summary: %s
                        """.formatted(
                                endpoint.method(),
                                endpoint.path(),
                                endpoint.summary()
                        )
                )
                .collect(java.util.stream.Collectors.joining("\n"));
    }
}