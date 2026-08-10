package ir.platco.ai.ai.dto;

public record AiAnalysisRequest(
        String topic,
        String audience,
        String context
) {
}