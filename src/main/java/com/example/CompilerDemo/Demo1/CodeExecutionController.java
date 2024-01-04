package com.example.CompilerDemo.Demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// CodeExecutionController.java
@RestController
public class CodeExecutionController {

    @Autowired
    private CodeFileManager codeFileManager;

    @Autowired
    private CodeExecutionService codeExecutionService;


    @PostMapping("/")
    public CodeExecutionResult runCode(@RequestBody CodeExecutionRequest request) {
        try {
            CodeExecutionResult result = codeExecutionService.runCode(request);

            return result;
        } catch (Exception e) {
            System.out.println(e);
            CodeExecutionResult codeExecutionResult = handleException(e);
            return codeExecutionResult;
        }
    }

    private <T> T handleException(Exception e) {
        throw new RuntimeException("An error occurred: " + e.getMessage());
    }
}

