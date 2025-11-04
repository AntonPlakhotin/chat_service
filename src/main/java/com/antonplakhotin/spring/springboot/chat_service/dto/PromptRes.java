package com.antonplakhotin.spring.springboot.chat_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromptRes {

    private long id;
    private String title;
    private String authorId;
    private String description;
    private String text;
}
