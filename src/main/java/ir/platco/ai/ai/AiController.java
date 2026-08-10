package ir.platco.ai.ai;

import ir.platco.ai.ai.dto.AiAnalysisRequest;
import ir.platco.ai.ai.model.AiAnalysisResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
public class AiController {

    private final ChatClient chatClient;

    public AiController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("/api/ai")
    public AiAnalysisResponse analyze(
            @RequestBody AiAnalysisRequest request
    ) {

        return chatClient
                .prompt()
                .system("""
                    You are an expert API Management engineer.

                    Analyze the requested technical topic.

                    Adapt your explanation to the specified audience
                    and technical context.

                    Return a structured analysis containing:
                    - title
                    - category
                    - difficulty
                    - summary
                    - key concepts

                    Category must be one of:
                    SECURITY,
                    API_MANAGEMENT,
                    ARCHITECTURE,
                    PROGRAMMING,
                    DATABASE,
                    OTHER

                    Difficulty must be one of:
                    BEGINNER,
                    INTERMEDIATE,
                    ADVANCED
                    """)
                .user("""
                    Topic: %s
                    Audience: %s
                    Context: %s
                    """.formatted(
                        request.topic(),
                        request.audience(),
                        request.context()
                ))
                .call()
                .entity(AiAnalysisResponse.class);
    }
}