package ir.platco.ai.ai;

import ir.platco.ai.ai.dto.AiAnalysisRequest;
import ir.platco.ai.ai.model.AiAnalysisResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public AiAnalysisResponse analyze(AiAnalysisRequest request) {

        String templateText = """
            You are an expert API Management engineer.

            Analyze the following technical topic.

            Topic:
            {topic}

            Target Audience:
            {audience}

            Technical Context:
            {context}

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
            """;

        PromptTemplate template = new PromptTemplate(templateText);

        var prompt = template.create(
                Map.of(
                        "topic", request.topic(),
                        "audience", request.audience(),
                        "context", request.context()
                )
        );

        return chatClient
                .prompt(prompt)
                .call()
                .entity(AiAnalysisResponse.class);
    }
}
