
package com.interview.interview_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.interview_service.modal.InterviewEntity;
import com.interview.interview_service.repository.InterviewRepository;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Service
public class InterviewLinkGeneration {

    private final WebClient webClient;
    private final InterviewRepository interviewRepository;

    @Autowired
    public InterviewLinkGeneration(WebClient.Builder webClientBuilder, InterviewRepository interviewRepository) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.cluster.dyte.in/v2/meetings")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeaders(
                        headers -> headers.setBasicAuth("955e223b-76a8-4c24-a9c6-ecbfea717290", "26425221301ce90b9244"))

                .build();
        this.interviewRepository = interviewRepository;
    }

    // public Mono<String> createMeeting(InterviewEntity interviewEntity) {
    // System.out.println("im here inside the create metting");
    // if (interviewEntity == null) {
    // System.err.println("Error: interviewEntity is null");
    // return Mono.error(new IllegalStateException("interviewEntity is null"));
    // }

    // String requestBody = "{\n" +
    // " \"title\": \"iosys\",\n" +
    // " \"preferred_region\": \"ap-south-1\",\n" +
    // " \"record_on_start\": false,\n" +
    // " \"live_stream_on_start\": false\n" +
    // "}";

    // return this.webClient.post()
    // .bodyValue(requestBody)
    // .retrieve()
    // .bodyToMono(String.class)
    // .flatMap(result -> {
    // try {
    // ObjectMapper objectMapper = new ObjectMapper();
    // JsonNode jsonNode = objectMapper.readTree(result);
    // String id = jsonNode.path("data").path("id").asText();
    // System.out.println("Meeting ID: " + id);
    // interviewEntity.setMeetingId(id);
    // interviewRepository.save(interviewEntity);
    // return Mono.just(id);
    // } catch (Exception e) {
    // System.err.println("Error parsing JSON response: " + e.getMessage());
    // return Mono.error(e);
    // }
    // });
    // }

    public Mono<String> createMeeting(InterviewEntity interviewEntity) {
        System.out.println("Inside createMeeting method");

        // Check if the interviewEntity is null
        if (interviewEntity == null) {
            System.err.println("Error: interviewEntity is null");
            return Mono.error(new IllegalStateException("interviewEntity is null"));
        }

        String requestBody = "{\n" +
                "    \"title\": \"iOSYS\",\n" +
                "    \"preferred_region\": \"ap-south-1\",\n" +
                "    \"record_on_start\": false,\n" +
                "    \"live_stream_on_start\": false\n" +
                "}";

        return this.webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse -> {
                    // Handle non-2xx responses
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseBody -> {
                                System.err.println("Error response from server: " + responseBody);
                                return Mono.error(new RuntimeException(
                                        "Error response with status: " + clientResponse.statusCode()));
                            });
                })
                .bodyToMono(String.class)
                .flatMap(result -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(result);
                        String id = jsonNode.path("data").path("id").asText();
                        System.out.println("Meeting ID: " + id);
                        interviewEntity.setMeetingId(id);
                        interviewRepository.save(interviewEntity);
                        return Mono.just(id);
                    } catch (Exception e) {
                        System.err.println("Error parsing JSON response: " + e.getMessage());
                        return Mono.error(new RuntimeException("Failed to parse JSON response: " + e.getMessage(), e));
                    }
                })
                .doOnError(e -> {
                    // Log the error
                    System.err.println("Error occurred in createMeeting: " + e.getMessage());
                });
    }

    public void manageIdAndSendEmail() {

        List<InterviewEntity> data = interviewRepository
                .findBySchedulingEmailTrueAndIsScheduledTrueAndIsInterviewLinkEmailSentTrue();
        System.out.println("im here in this method");
        for (InterviewEntity interviewEntity : data) {
            if (interviewEntity.getAuthToken() == null) {
                String email = interviewEntity.getEmail();
                String meetingId = interviewEntity.getMeetingId();
                System.out.println("this is the meetting id");
                System.out.println("meetingId" + meetingId);
                String baseUrl = "https://api.dyte.io/v2";
                String url = baseUrl + "/meetings/" + meetingId + "/participants";
                // String jsonBody = """
                // {
                // "name":"iosys" ,
                // "picture": "https://i.imgur.com/test.jpg",
                // "preset_name": "group_call_host",
                // "custom_participant_id": "string"
                // }
                // """;
                String candidateName = interviewEntity.getCandidateName();
                String sanitizedCandidateName = candidateName.replace("\"", "\\\"").replace("\n", "").replace("\r", "")
                        .replace("\t", "");

                System.out.println("this is nameeeeeee" + sanitizedCandidateName);

                String jsonBody = String.format("""
                        {
                        "name": "%s",
                        "picture": "https://i.imgur.com/test.jpg",
                        "preset_name": "group_call_host",
                        "custom_participant_id": "string"
                        }
                        """, sanitizedCandidateName);
                try {
                    String response = this.webClient.post()
                            .uri(url)
                            .body(BodyInserters.fromValue(jsonBody))
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();
                    System.out.println("im here for the token ");

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response);
                    JsonNode dataNode = rootNode.path("data");
                    String token = dataNode.path("token").asText();
                    interviewEntity.setAuthToken(token);
                    interviewEntity.setTokenGenerated(true);
                    interviewRepository.save(interviewEntity);
                    System.out.println("Response body: " + response);
                    System.out.println("im here for the token");
                } catch (WebClientResponseException e) {
                    System.err.println("Error response: " + e.getResponseBodyAsString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("the authtoken is already present");
            }
        }
    }
}
