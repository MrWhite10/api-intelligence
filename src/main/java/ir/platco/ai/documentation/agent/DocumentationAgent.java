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
                chatClientBuilder
                        .build();
    }

    public GeneratedDocumentation generate(
            DocumentationRequestContext context,
            OpenApiDocumentationTools tools
    ) {

        String prompt =
                buildPrompt(
                        context
                );

        return chatClient
                .prompt()
                .user(prompt)
                .tools(tools)
                .call()
                .entity(
                        GeneratedDocumentation.class
                );
    }

    private String buildPrompt(
            DocumentationRequestContext context
    ) {

        return """
                Generate structured API documentation.

                You are an API documentation assistant.

                Use the available OpenAPI tools to retrieve
                detailed information about API operations.

                Rules:

                - Do not invent API facts.
                - Use tool results as the source of truth.
                - Generate documentation for all operations
                  available in the API context.
                - Preserve method, path, parameters,
                  request body and response information.
                - If information does not exist,
                  leave the corresponding field null or empty.
                - If an operation does not define a request body,
                  set requestBody to null.
                - If a schema does not exist in the OpenAPI specification,
                  set schema to null.
                - Never create an empty schema object.
                - Do not populate schema with empty strings.
                - If requestBody does not exist, set requestBody to null.
                - If a response has no response content schema,
                  set response.schema to null.
                - Do not invent fields, examples, formats, or schemas.

                API information:

                Name: %s
                Version: %s
                Description: %s

                Available operations:

                %s
                """.formatted(
                context.metadata().title(),
                context.metadata().version(),
                context.metadata().description(),
                buildEndpointsContext(
                        context
                )
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
                .collect(java.util.stream.Collectors.joining("\n"));
    }
}