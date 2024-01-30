package com.example.CompilerDemo.Service;
import com.example.CompilerDemo.DTO.CodeExecutionRequest;
import com.example.CompilerDemo.DTO.CodeExecutionResult;
import com.example.CompilerDemo.DTO.CodeFile;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CodeExecutionService {
    private final CodeFileManager codeFileManager;

    public CodeExecutionService(CodeFileManager codeFileManager) {
        this.codeFileManager = codeFileManager;
    }
    public CodeExecutionResult runCode(CodeExecutionRequest request , String id) throws Exception {
        String language = request.getLanguage();
        String code = request.getCode();
        String input = request.getInput();

        validateRequest(language, code);
        System.out.println("AfterValidation");

        CodeFile codeFile = codeFileManager.createCodeFile(language, code,id);
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

        long startTime = System.currentTimeMillis();
        CodeExecutionResult result = new CodeExecutionResult();
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

                long compilationTime = System.currentTimeMillis() - startTime;
                System.out.println("Compilation Time: " + compilationTime + " milliseconds");

                // Measure storage capacity after compilation
                Path compiledPath = Paths.get(getOutputFilePath(jobID, outputExt));
                long compiledBinarySize = compiledPath.toFile().length();
                System.out.println("Compiled Binary Size: " + compiledBinarySize + " bytes");

                // Execute compiled code
//                ProcessBuilder processBuilder1 = new ProcessBuilder(compiledBinaryPath.toString());
//                Process process1 = processBuilder1.start();

                // Existing code...
//
//                process1.waitFor();
//                long endTime = System.currentTimeMillis(); // Measure end time
//                long executionTime = endTime - startTime;
//                System.out.println("Execution Time: " + executionTime + " milliseconds");

                // Measure storage capacity after execution
                long outputSize = compiledPath.toFile().length();
                System.out.println("Output Size: " + outputSize + " bytes");
                result.setStorageCapacity(outputSize+" Bytes");
            if (compilationProcess.exitValue() == 0) {

                String chmodCommand = "chmod +x " + compiledBinaryPath;
                Process chmodProcess = Runtime.getRuntime().exec(chmodCommand);
                chmodProcess.waitFor();
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                result.setExeTime(executionTime+ " milliseconds");
                System.out.println("Execution Time: " + executionTime + " milliseconds");
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
            result.setOutput(output.toString());
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

                Thread timeoutThread = new Thread(() -> {
                    try {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("Output block : ");
                            output.append(line).append("\n");
                            long outputSize = output.toString().getBytes().length;
                            result.setStorageCapacity(outputSize+" Bytes");
                        }
                        process.waitFor();
                        long endTime = System.currentTimeMillis();
                        long executionTime = endTime - startTime;
                        result.setExeTime(executionTime+ " milliseconds");

                        System.out.println("Execution Time: " + executionTime + " milliseconds");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException("RunTime Exceeded");
                    }
                    if (process.isAlive()) {
                        System.out.println("Process exceeded the timeout. Destroying...");
                        process.destroy();
                    }
                });

                timeoutThread.start();
                timeoutThread.join(1 * 1000);
                process.destroy();
                    result.setOutput(output.toString());

                    String k = codeFile.getFileName().replaceAll("\\.\\w+", "");

                    result.setOutputExt(k);

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

