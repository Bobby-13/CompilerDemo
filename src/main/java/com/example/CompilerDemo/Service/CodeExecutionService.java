package com.example.CompilerDemo.Service;
import com.example.CompilerDemo.DTO.CodeExecutionRequest;
import com.example.CompilerDemo.DTO.CodeExecutionResult;
import com.example.CompilerDemo.DTO.CodeFile;
import com.example.CompilerDemo.Service.CodeFileManager;
import com.example.CompilerDemo.Service.LanguageConstants;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CodeExecutionService {

    private final CodeFileManager codeFileManager;
        public CodeExecutionService(CodeFileManager codeFileManager) {
        this.codeFileManager = codeFileManager;
    }

    public CodeExecutionResult runCode(CodeExecutionRequest request) throws Exception {
        String language = request.getLanguage();
        String code = request.getCode();
        String input = request.getInput();

        validateRequest(language, code);
        System.out.println("AfterValidation");

        CodeFile codeFile = codeFileManager.createCodeFile(language, code);
        System.out.println("code file :" + codeFile);
        CodeExecutionResult result = executeCode(language, codeFile, input);

        try {
            System.out.println("After code Execution result : " + result);

            return result;
        }
        finally {
            codeFileManager.removeCodeFile(codeFile.getJobID(), language, result.getOutputExt());
        }
    }

    private void validateRequest(String language, String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("No code found to execute.");
        }

        if (!LanguageConstants.supportedLanguages.contains(language)) {
            throw new IllegalArgumentException("Please enter a valid language.");
        }
    }


    public CodeExecutionResult executeCode(String language, CodeFile codeFile, String input) throws Exception {
        String jobID = codeFile.getJobID();
        String codeFilePath = codeFile.getFilePath();
        String outputExt = getOutputExtension(language);

        System.out.println("job Id :" + jobID);
        System.out.println("codeFile path :" + codeFilePath);
        String executeCommand;
        String[] executionArgs;
        String compiledBinaryPath = getOutputFilePath(jobID, outputExt);
        switch (language) {
            case "java":
                executeCommand = "java";
                executionArgs = new String[]{codeFilePath};
                break;
            case "cpp":
                executeCommand = "g++";
                executionArgs = new String[]{codeFilePath, "-o", compiledBinaryPath};
                String formattedCommand = String.join(" ", executionArgs);
                System.out.println("Formatted Command: " + formattedCommand);
                break;
            case "py":
                executeCommand = "python3";
                executionArgs = new String[]{codeFilePath};
                break;
            case "c":
                executeCommand = "gcc";
                executionArgs = new String[]{codeFilePath, "-o", compiledBinaryPath};
                System.out.println("Formatted C Path :" + (String.join(" ", executionArgs)));
                System.out.println(getOutputFilePath(jobID, outputExt));
                break;
            case "js":
                executeCommand = "node";
                executionArgs = new String[]{codeFilePath};
                break;
            case "go":
                executeCommand = "go";
                executionArgs = new String[]{"run", codeFilePath};
                break;
            default:
                throw new UnsupportedOperationException("Language not supported: " + language);
        }
        try {
            if(executeCommand.equals("g++") || executeCommand.equals("gcc")){
            String command = "chmod +x " + codeFilePath;
            Process process3 = Runtime.getRuntime().exec(command);
            process3.waitFor();

            List<String> cmd;

                cmd = Arrays.asList(executeCommand, codeFilePath, "-o", compiledBinaryPath);


            ProcessBuilder compilationProcessBuilder = new ProcessBuilder(cmd);
            Process compilationProcess = compilationProcessBuilder.start();
            compilationProcess.waitFor();

            if (compilationProcess.exitValue() == 0) {

                String chmodCommand = "chmod +x " + compiledBinaryPath;
                Process chmodProcess = Runtime.getRuntime().exec(chmodCommand);
                chmodProcess.waitFor();
            } else {
                throw new RuntimeException("Compilation failed");
            }

                System.out.println("After Compilation ::");

                ProcessBuilder processBuilder1 = new ProcessBuilder(compiledBinaryPath);
                Process process1 = processBuilder1.start();

            if (input != null && !input.isEmpty()) {
                try (OutputStream outputStream = process1.getOutputStream()) {
                    outputStream.write(input.getBytes());
                    System.out.println("Input field :"+input.getBytes());
                }
            }
            process1.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process1.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Output block : ");
                output.append(line).append("\n");
                System.out.println("-->"+output);
            }

                System.out.println("Code Executed :");

            int exitCode = process1.exitValue();

            CodeExecutionResult result = new CodeExecutionResult();
            result.setOutput(output.toString());
            result.setExitCode(exitCode);
            result.setOutputExt(outputExt);

                return result;

            }
            else{
                ProcessBuilder processBuilder = new ProcessBuilder();
                List<String> commandList = new ArrayList<>();
                commandList.add(executeCommand);
                commandList.addAll(Arrays.asList(executionArgs));
                processBuilder.command(commandList);
                processBuilder.directory(new File(System.getProperty("user.dir")));
                processBuilder.redirectErrorStream(true);

                    Process process = processBuilder.start();
                    System.out.println(process.getOutputStream());
                    if (input != null && !input.isEmpty()) {
                        process.getOutputStream().write(input.getBytes());
                        process.getOutputStream().close();
                    }


                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Output block : ");
                        output.append(line).append("\n");
                    }

                    boolean finished = process.waitFor(30, TimeUnit.SECONDS);

                    if (!finished) {
                        process.destroy();
                        throw new RuntimeException("Code execution timed out.");
                    }

                    int exitCode = process.exitValue();

                    CodeExecutionResult result = new CodeExecutionResult();
                    result.setOutput(output.toString());
                    result.setExitCode(exitCode);
                    result.setOutputExt(outputExt);

                    return result;
            }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                System.out.println(e);
                throw new RuntimeException("Error executing code: " + e.getMessage(), e);
            }
        }

        private String getOutputExtension (String language){
            switch (language) {
                case "java":
                case "js":
                case "go":
                    return ".out";
                case "cpp":
                case "c":
                case "py":
                    return "";

                default:
                    throw new UnsupportedOperationException("Language not supported: " + language);
            }
        }


        private String getOutputFilePath (String jobID, String outputExt){
            return Paths.get("outputs", jobID + "" + outputExt).toString();
        }
    }

