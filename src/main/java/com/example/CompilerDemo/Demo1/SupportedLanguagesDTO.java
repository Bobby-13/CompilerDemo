package com.example.CompilerDemo.Demo1;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupportedLanguagesDTO {
    private List<SupportedLanguageInfo> supportedLanguages;

}
