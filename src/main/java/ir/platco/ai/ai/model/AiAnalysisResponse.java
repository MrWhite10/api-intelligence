package ir.platco.ai.ai.model;

import java.util.List;

public record AiAnalysisResponse(
        String title,
        AiCategory category,
        Difficulty difficulty,
        String summary,
        List<String> keyConcepts
) {
}