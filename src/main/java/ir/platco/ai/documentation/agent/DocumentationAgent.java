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

                        The documentation template contains placeholders.

                        Placeholders use this format:

                        {{placeholder_name}}

                        Your task is to replace every placeholder with the
                        appropriate generated content.

                        Placeholder definitions:

                        {{api_name}}

                        Replace with the API title.

                        {{api_overview}}

                        Generate a concise overview of the API based only
                        on the provided API metadata.

                        Do not invent API capabilities that are not defined
                        in the OpenAPI specification.

                        {{api_information}}

                        Generate a Markdown table containing exactly these rows:

                        - API Name
                        - Version
                        - Description

                        Do not add any additional fields.

                        {{endpoints_table}}

                        Generate a Markdown table containing exactly these
                        columns:

                        - Method
                        - Path
                        - Summary

                        Include every available API endpoint.

                        {{endpoint_details}}

                        Generate detailed documentation for every available
                        API endpoint.

                        For each endpoint include:

                        - Method
                        - Path
                        - Summary
                        - Description
                        - Parameters
                        - Request body
                        - Responses

                        Use the available tools to retrieve detailed
                        information about every endpoint.

                        {{request_details}}

                        Generate a consolidated documentation section for
                        request parameters and request bodies.

                        Include information only when it is defined in the
                        OpenAPI specification.

                        Use the available tools when detailed endpoint
                        information is required.

                        {{response_details}}

                        Generate a consolidated documentation section for
                        response status codes and response information.

                        Include information only when it is defined in the
                        OpenAPI specification.

                        Use the available tools when detailed endpoint
                        information is required.

                        Before generating the final documentation:

                        1. Identify all available API endpoints.

                        2. Retrieve detailed information for every endpoint
                           using the available tools.

                        3. Use the retrieved information to generate the
                           documentation sections.

                        Rules:

                        - Replace every placeholder in the template.

                        - Do not leave any placeholder unchanged.

                        - Do not add explanations about placeholders.

                        - Do not add instructions from the template to the
                          final document.

                        - Preserve the existing Markdown structure.

                        - Do not add sections that do not exist in the
                          provided template.

                        - Do not add fields that are not requested by the
                          placeholder definitions.

                        - Do not invent API endpoints.

                        - Do not invent parameters.

                        - Do not invent request bodies.

                        - Do not invent response fields.

                        - Do not invent response status codes.

                        - If information does not exist in the OpenAPI
                          specification, clearly state that it is not defined.

                        - Preserve the language of the documentation
                          template.

                        - Return only the final Markdown documentation.
                        """)
                .user(
                        buildUserPrompt(
                                context,
                                endpointsContext
                        )
                )
                .tools(tools)
                .call()
                .content();

        return new GeneratedDocumentation(
                content
        );
    }

    private String buildUserPrompt(
            DocumentationRequestContext context,
            String endpointsContext
    ) {

        return """
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