package com.example.CompilerDemo.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeExecutionRequest {
    private String language;
    private String code;
    private String input;
}