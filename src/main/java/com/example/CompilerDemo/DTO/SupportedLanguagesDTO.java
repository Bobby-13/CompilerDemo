package com.example.CompilerDemo.DTO;

import com.example.CompilerDemo.DTO.SupportedLanguageInfo;
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
