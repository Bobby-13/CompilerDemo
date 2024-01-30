package com.example.CompilerDemo.Controller;

import com.example.CompilerDemo.DTO.CodeExecutionRequest;
import com.example.CompilerDemo.DTO.CodeExecutionResult;
import com.example.CompilerDemo.Service.CodeExecutionService;
import com.example.CompilerDemo.Service.CodeFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CodeExecutionController {

    @Autowired
    private CodeFileManager codeFileManager;

    @Autowired
    private CodeExecutionService codeExecutionService;

    @PostMapping("/{id}")
    public CodeExecutionResult runCode(@RequestBody CodeExecutionRequest request,@PathVariable String id) {
        try {
            CodeExecutionResult result = codeExecutionService.runCode(request,id);
            return result;
        } catch (Exception e) {
            System.out.println(e);
//            CodeExecutionResult codeExecutionResult = handleException(e);
            return CodeExecutionResult.builder().error("TimeLimit Exceeded").exeTime("TimeLimit Exceeded").build();
        }
    }

    private <T> T handleException(Exception e) {
        throw new RuntimeException("An error occurred: " + e.getMessage());
    }
}

