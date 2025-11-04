package com.antonplakhotin.spring.springboot.chat_service.controller;

import com.antonplakhotin.spring.springboot.chat_service.dto.*;
import com.antonplakhotin.spring.springboot.chat_service.service.LlmService;
import com.antonplakhotin.spring.springboot.chat_service.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private LlmService llmService;

    @PostMapping("/create")
    public ResponseEntity<Long> createChat(@RequestBody CreateChatRq createChatRq) {
        Long chatId = storageService.createChat(createChatRq);
        if (chatId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(chatId);
    }

    @PostMapping("/setPrompt")
    public ResponseEntity<Void> setPromptToChat(@RequestBody SetPromptRq setPromptRq) {
        boolean success = storageService.setPrompt(setPromptRq);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/write")
    public ResponseEntity<WriteToChatRs> writeToChat(@RequestBody WriteToChatRq writeToChatRq) {
        if (storageService.getChat(writeToChatRq.getChatId()) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        WriteToChatRs response = llmService.askLlm(writeToChatRq);
        return ResponseEntity.ok(response);
    }

    //список чатов для пользователя(id, имя)
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<ChatRes>> getAllChats(@PathVariable String userId) {
        List<ChatRes> chats = storageService.getAllChats(userId);
        if (chats == null || chats.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(chats);
    }

    //список сообщений для чата(принимает id)
    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<MessageRes>> getChatMessages(@PathVariable long chatId) {
        // обращаемся к storageService, который вызывает /api/{chatId}/messages
        List<MessageRes> messages = storageService.getMessages(chatId);

        if (messages == null || messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatRes> getChat(@PathVariable long chatId) {
        ChatRes chat = storageService.getChat(chatId);
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chat);
    }

    // Переименовать чат
    @PutMapping("/rename")
    public ResponseEntity<Void> renameChat(@RequestBody RenameChatRq renameChatRq) {
        boolean success = storageService.renameChat(renameChatRq);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // Удалить чат
    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable long chatId) {
        boolean success = storageService.deleteChat(chatId);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

