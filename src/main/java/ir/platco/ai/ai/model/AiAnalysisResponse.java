package ir.platco.ai.ai.model;

import java.util.List;

public record AiAnalysisResponse(
        String title,
        String category,
        String difficulty,
        String summary,
        List<String> keyConcepts
) {
}