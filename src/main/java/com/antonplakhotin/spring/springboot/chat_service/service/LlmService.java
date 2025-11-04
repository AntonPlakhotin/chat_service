package com.antonplakhotin.spring.springboot.chat_service.service;

import com.antonplakhotin.spring.springboot.chat_service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LlmService {
    @Autowired
    private StorageService storageService;

    @Autowired
    private RestTemplate restTemplate;

    private final String llmAdapterUrl = "http://localhost:8105/api/dialog/ask";

    public WriteToChatRs askLlm(WriteToChatRq writeToChatRq) {

        MessageRq userMessage = MessageRq.builder()
                .chatId(writeToChatRq.getChatId())
                .author(Author.USER)
                .content(writeToChatRq.getMessage())
                .build();
        storageService.saveMessage(userMessage);

        AskLlmRq askLlmRq = AskLlmRq.builder()
                .chatId(writeToChatRq.getChatId())
                .model(writeToChatRq.getModel())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AskLlmRq> requestEntity = new HttpEntity<>(askLlmRq, headers);

        ResponseEntity<WriteToChatRs> response;
        try {
            response = restTemplate.exchange(
                    llmAdapterUrl,
                    HttpMethod.POST,
                    requestEntity,
                    WriteToChatRs.class);
        } catch (Exception e) {
            return WriteToChatRs.builder()
                    .message("Error calling LLM adapter: " + e.getMessage())
                    .build();
        }

        WriteToChatRs llmResponse = response.getBody();

        if (llmResponse != null && llmResponse.getMessage() != null) {
            MessageRq llmMessage = MessageRq.builder()
                    .chatId(writeToChatRq.getChatId())
                    .author(Author.BOT)
                    .content(llmResponse.getMessage())
                    .build();
            storageService.saveMessage(llmMessage);
        }

        return llmResponse;
    }
}
