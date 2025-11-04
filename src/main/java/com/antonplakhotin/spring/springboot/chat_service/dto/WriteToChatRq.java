package com.antonplakhotin.spring.springboot.chat_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WriteToChatRq {
    long chatId;
    String message;
    String model;
}
