package com.example.CompilerDemo.Demo1;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeFile {
    private String fileName;
    private String filePath;
    private String jobID;
}
