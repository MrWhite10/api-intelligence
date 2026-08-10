package ir.platco.ai.ai;

import ir.platco.ai.ai.model.AiAnalysisResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

    private final ChatClient chatClient;

    public AiController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/api/ai")
    public AiAnalysisResponse ask(
            @RequestParam String message
    ) {

        return chatClient
                .prompt()
                .system("""
                    You are an expert API Management engineer.

                    Analyze the user's technical topic.

                    The response must contain:
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
                .user(message)
                .call()
                .entity(AiAnalysisResponse.class);
    }
}