package com.example.CompilerDemo.Demo1;

import java.util.Arrays;
import java.util.List;

public class LanguageConstants {
    public static final List<String> supportedLanguages = Arrays.asList("java", "cpp", "py", "c", "js", "go", "cs");

    public static String getCompilerInfoCommand(String language) {
        // Implement logic to map each language to its corresponding compiler info command
        System.out.println("language :"+language);
        switch (language) {
            case "java":
                return "java --version";
            case "cpp":
                return "g++ --version";
            case "py":
                return "python3 --version";
            case "c":
                return "gcc --version";
            case "js":
                return "node --version";
            case "go":
                return "go version";
            case "cs":
                return "mcs --version";
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }
}
