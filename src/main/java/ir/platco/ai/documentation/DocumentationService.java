package ir.platco.ai.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import ir.platco.ai.documentation.agent.tool.OpenApiDocumentationTools;
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
        this.chatClient =
                chatClientBuilder.build();
    }

    public GeneratedDocumentation generate(
            OpenAPI openAPI,
            OpenApiMetadata metadata,
            String template
    ) {

        OpenApiDocumentationTools tools =
                new OpenApiDocumentationTools(
                        openAPI
                );

        String endpointsContext =
                buildEndpointsContext(
                        metadata
                );

        String content = chatClient
                .prompt()
                .system("""
                        You are an expert technical API documentation writer.

                        Your task is to generate complete API documentation
                        based only on the provided OpenAPI specification.

                        You have access to tools that can retrieve detailed
                        information about API operations.

                        Available tool capabilities include retrieving:
                        - operation details
                        - parameters
                        - operation descriptions
                        - response status codes
                        - response descriptions

                        Use the available tools whenever the provided context
                        does not contain enough information to accurately
                        complete the documentation.

                        Rules:

                        - Follow the structure of the provided documentation
                          template.

                        - Do not include instructions from the template in
                          the final document.

                        - Do not invent API endpoints.

                        - Do not invent request parameters.

                        - Do not invent request bodies.

                        - Do not invent response fields.

                        - Do not invent response status codes.

                        - When detailed information about an endpoint is
                          required, use the available tools.

                        - If information does not exist in the OpenAPI
                          specification, clearly state that it is not defined.

                        - Preserve the language of the documentation template.

                        - Return only the final Markdown documentation.
                        """)
                .user("""
                        Documentation Template:

                        %s

                        API Metadata:

                        Name: %s

                        Version: %s

                        Description: %s

                        Available Endpoints:

                        %s
                        """.formatted(
                        template,
                        metadata.title(),
                        metadata.version(),
                        metadata.description(),
                        endpointsContext
                ))
                .tools(tools)
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
                .collect(
                        java.util.stream.Collectors.joining(
                                "\n"
                        )
                );
    }
}