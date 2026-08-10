package ir.platco.ai.ai;

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
    public String ask(
            @RequestParam String message
    ) {

        return chatClient
                .prompt()
                .system("""
                        You are an expert API Management engineer.
        
                        Your job is to explain API-related technical
                        concepts clearly and practically.
        
                        Prefer examples related to:
                        - REST APIs
                        - OAuth2
                        - OpenAPI
                        - WSO2 API Manager
                        - API Security
        
                        Keep answers concise but technically accurate.
                        """)
                .user(message)
                .call()
                .content();
    }
}