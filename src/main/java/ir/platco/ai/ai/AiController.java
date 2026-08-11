package ir.platco.ai.ai;

import ir.platco.ai.ai.dto.AiAnalysisRequest;
import ir.platco.ai.ai.model.AiAnalysisResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/api/ai")
    public AiAnalysisResponse analyze(
            @RequestBody AiAnalysisRequest request
    ) {
        return aiService.analyze(request);
    }
}