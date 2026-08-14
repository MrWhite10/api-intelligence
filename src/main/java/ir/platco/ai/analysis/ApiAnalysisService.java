package ir.platco.ai.analysis;

import ir.platco.ai.analysis.dto.ApiAnalysisResponse;
import ir.platco.ai.analysis.dto.RuleViolation;
import ir.platco.ai.openapi.dto.OpenApiMetadata;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiAnalysisService {

    private final ChatClient chatClient;

    public ApiAnalysisService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public ApiAnalysisResponse analyze(OpenApiMetadata metadata, List<RuleViolation> violations) {
        String endpointsContext = buildEndpointsContext(metadata);
        String ruleContext = buildRuleContext(violations);

        return chatClient
                .prompt()
                .system("""
                You are an expert API architect and API security engineer.

                You will receive:

                1. OpenAPI metadata
                2. Deterministic rule violations detected by the application

                The rule violations are facts already verified by
                the application. Do not contradict them.

                Your responsibilities are:

                - Prioritize the detected issues
                - Explain their technical impact
                - Suggest practical improvements
                - Identify additional issues only when supported
                  by the provided API metadata

                Scores must be between 0 and 100.

                Do not invent endpoints or security configurations.
                """)
                .user("""
                API:

                Name: %s
                Version: %s
                Description: %s

                Endpoints:

                %s

                Verified Rule Violations:

                %s
                """.formatted(
                        metadata.title(),
                        metadata.version(),
                        metadata.description(),
                        endpointsContext,
                        ruleContext
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

    private String buildRuleContext(List<RuleViolation> violations) {

        if (violations.isEmpty()) {
            return "No deterministic rule violations were found.";
        }

        return violations.stream()
                .map(violation ->
                        """
                        Rule: %s
                        Severity: %s
                        Path: %s
                        Method: %s
                        Message: %s
                        """.formatted(
                                violation.ruleId(),
                                violation.severity(),
                                violation.path(),
                                violation.method(),
                                violation.message()
                        )
                )
                .collect(java.util.stream.Collectors.joining("\n"));
    }

}