package com.example.CompilerDemo.Demo1;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeExecutionResult {
    private String output;
    private String error;
    private String language;
    private String info;
    private int exitCode;
    private String outputExt;
}
