package com.antonplakhotin.spring.springboot.chat_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AskLlmRq {
    long chatId;
    String model;
}
