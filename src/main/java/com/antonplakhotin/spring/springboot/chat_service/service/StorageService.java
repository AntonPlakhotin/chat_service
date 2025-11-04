package com.antonplakhotin.spring.springboot.chat_service.service;

import com.antonplakhotin.spring.springboot.chat_service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class StorageService {
    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8099/api";

    public ChatRes getChat(long chatId) {
        try {
            String url = baseUrl + "/chat/" + chatId;
            ResponseEntity<ChatRes> response = restTemplate.getForEntity(url, ChatRes.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error fetching chat " + chatId + ": " + e.getMessage());
            return null;
        }
    }

    public PromptRes getPrompt(long chatId) {
        try {
            String url = baseUrl + "/chatPrompt/" + chatId;
            ResponseEntity<PromptRes> response = restTemplate.getForEntity(url, PromptRes.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error fetching prompt for chatId " + chatId + ": " + e.getMessage());
            return null;
        }
    }

    public List<ChatRes> getAllChats(String userId) {
        try {
            String url = baseUrl + "/chats/" + userId;

            ResponseEntity<List<ChatRes>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ChatRes>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error fetching chats for user " + userId + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Long createChat(CreateChatRq createChatRq) {
        try {
            String url = baseUrl + "/chat";
            ResponseEntity<Long> response = restTemplate.postForEntity(url, createChatRq, Long.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error creating chat: " + e.getMessage());
            return null;
        }
    }

    public boolean setPrompt(SetPromptRq setPromptRq) {
        try {
            String url = baseUrl + "/chat/setPrompt";
            ResponseEntity<Void> response = restTemplate.postForEntity(url, setPromptRq, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Error setting prompt: " + e.getMessage());
            return false;
        }
    }

    public boolean renameChat(RenameChatRq renameChatRq) {
        try {
            String url = baseUrl + "/chat/rename";
            restTemplate.put(url, renameChatRq);
            return true;
        } catch (Exception e) {
            System.err.println("Error renaming chat: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteChat(long chatId) {
        try {
            String url = baseUrl + "/chat/delete/" + chatId;
            restTemplate.delete(url);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting chat " + chatId + ": " + e.getMessage());
            return false;
        }
    }

    public WriteToChatRs writeToChat(WriteToChatRq writeToChatRq) {
        try {
            String url = baseUrl + "/chat/write"; // предположительный endpoint
            ResponseEntity<WriteToChatRs> response = restTemplate.postForEntity(url, writeToChatRq, WriteToChatRs.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error writing to chat: " + e.getMessage());
            return WriteToChatRs.builder().message("Error: " + e.getMessage()).build();
        }
    }

    public Long saveMessage(MessageRq messageRq) {
        try {
            String url = baseUrl + "/message";
            ResponseEntity<Long> response = restTemplate.postForEntity(url, messageRq, Long.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error saving message: " + e.getMessage());
            return null;
        }
    }

    public List<MessageRes> getMessages(long chatId) {
        try {
            String url = baseUrl + "/" + chatId + "/messages";
            ResponseEntity<List<MessageRes>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MessageRes>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error fetching messages for chat " + chatId + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
