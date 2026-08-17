package ir.platco.ai.documentation.agent;

import ir.platco.ai.documentation.agent.model.DocumentationRequestContext;
import ir.platco.ai.documentation.agent.tool.OpenApiDocumentationTools;
import ir.platco.ai.documentation.model.GeneratedDocumentation;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class DocumentationAgent {

    private final ChatClient chatClient;

    public DocumentationAgent(
            ChatClient.Builder chatClientBuilder
    ) {
        this.chatClient =
                chatClientBuilder.build();
    }

    public GeneratedDocumentation generate(
            DocumentationRequestContext context
    ) {

        OpenApiDocumentationTools tools =
                new OpenApiDocumentationTools(
                        context.openAPI()
                );

        String endpointsContext =
                buildEndpointsContext(
                        context
                );

        String content = chatClient
                .prompt()
                .system("""
                        You are an autonomous API documentation agent.

                        Your responsibility is to generate accurate and
                        complete API documentation based only on the provided
                        OpenAPI specification and available tools.

                        You have access to tools that can retrieve detailed
                        information about API operations.

                        Before generating the final documentation:

                        1. Identify all available API endpoints.

                        2. For every endpoint, retrieve detailed information
                           using the available tools.

                        3. Use the retrieved information to document:
                           - operation descriptions
                           - parameters
                           - request information
                           - response status codes
                           - response descriptions

                        Rules:

                        - Follow the structure of the provided documentation
                          template.

                        - Do not include instructions from the template in
                          the final document.

                        - Do not invent API endpoints.

                        - Do not invent parameters.

                        - Do not invent request bodies.

                        - Do not invent response fields.

                        - Do not invent response status codes.

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
                        context.template(),
                        context.metadata().title(),
                        context.metadata().version(),
                        context.metadata().description(),
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
            DocumentationRequestContext context
    ) {

        return context.metadata()
                .endpoints()
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