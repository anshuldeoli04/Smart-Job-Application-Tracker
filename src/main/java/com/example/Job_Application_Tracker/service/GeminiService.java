package com.example.Job_Application_Tracker.service;

import com.example.Job_Application_Tracker.dto.AiMatchRequest;
import com.example.Job_Application_Tracker.model.AiMatchResult;
import com.example.Job_Application_Tracker.model.JobApplication;
import com.example.Job_Application_Tracker.repository.AiMatchResultRepository;
import com.example.Job_Application_Tracker.repository.JobApplicationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final JobApplicationRepository jobRepo;
    private final AiMatchResultRepository aiRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiMatchResult analyzeMatch(AiMatchRequest request) {

        JobApplication job = jobRepo.findById(request.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        String prompt = """
                Analyze this resume against the job description.
                Return ONLY a JSON object with no extra text, no markdown, no backticks.
                Format exactly like this:
                {"score": 75, "missingSkills": ["Docker", "Kafka"], "suggestions": ["Learn Docker basics", "Add measurable achievements"]}
                
                Resume:
                %s
                
                Job Description:
                %s
                """.formatted(request.getResumeText(), job.getJobDescription());

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
        Map<String, Object> body = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        RestClient restClient = RestClient.create();

        String response = restClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            String text = root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText();

            text = text.strip();
            if (text.startsWith("```")) {
                text = text.replaceAll("```json", "")
                        .replaceAll("```", "")
                        .strip();
            }

            JsonNode result = objectMapper.readTree(text);

            AiMatchResult aiResult = new AiMatchResult();
            aiResult.setJobApplication(job);
            aiResult.setMatchScore(result.path("score").asInt());
            aiResult.setMissingSkills(result.path("missingSkills").toString());
            aiResult.setSuggestions(result.path("suggestions").toString());
            aiResult.setResumeText(request.getResumeText());

            return aiRepo.save(aiResult);

        } catch (Exception e) {
            throw new RuntimeException("Gemini parse error: " + e.getMessage());
        }
    }
}
