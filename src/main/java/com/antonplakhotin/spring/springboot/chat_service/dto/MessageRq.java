package com.antonplakhotin.spring.springboot.chat_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRq {
    private long chatId;
    private Author author;
    private String content;
}
