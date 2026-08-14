package ir.platco.ai.analysis;

import ir.platco.ai.analysis.dto.ApiAnalysisResponse;
import ir.platco.ai.openapi.dto.OpenApiMetadata;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ApiAnalysisService {

    private final ChatClient chatClient;

    public ApiAnalysisService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public ApiAnalysisResponse analyze(OpenApiMetadata metadata) {
        String endpointsContext = buildEndpointsContext(metadata);
        return chatClient
                .prompt()
                .system("""
                        You are an expert API architect and API security engineer.

                        Analyze the provided OpenAPI metadata.

                        Evaluate:

                        1. API design quality
                        2. Security concerns
                        3. Documentation quality
                        4. Potential design problems
                        5. Improvements

                        Scores must be between 0 and 100.

                        Be practical and technically accurate.

                        Do not invent endpoints that are not present
                        in the provided API metadata.
                        """)
                .user("""
                        API Name:
                        %s

                        Version:
                        %s

                        Description:
                        %s

                        Endpoints:

                        %s
                        """.formatted(
                        metadata.title(),
                        metadata.version(),
                        metadata.description(),
                        endpointsContext
                ))
                .call()
                .entity(ApiAnalysisResponse.class);
    }

    private String buildEndpointsContext(OpenApiMetadata metadata) {

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