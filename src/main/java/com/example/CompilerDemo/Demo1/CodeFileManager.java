package com.example.CompilerDemo.Demo1;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class CodeFileManager {

    private static final String CODES_DIRECTORY = "codes";
    private static final String OUTPUTS_DIRECTORY = "outputs";

    public CodeFile createCodeFile(String language, String code) throws Exception {
        if (!Files.exists(Paths.get(CODES_DIRECTORY))) {
            Files.createDirectories(Paths.get(CODES_DIRECTORY));
        }

        if (!Files.exists(Paths.get(OUTPUTS_DIRECTORY))) {
            Files.createDirectories(Paths.get(OUTPUTS_DIRECTORY));
        }

        String jobID = UUID.randomUUID().toString();
        String fileName = String.format("%s.%s", jobID, language);
        String filePath = Paths.get(CODES_DIRECTORY, fileName).toString();

        Files.write(Paths.get(filePath), code.getBytes());

        return new CodeFile(fileName, filePath, jobID);
    }

    public void removeCodeFile(String uuid, String lang, String outputExt) throws Exception {
        String codeFilePath = Paths.get(CODES_DIRECTORY, String.format("%s.%s", uuid, lang)).toString();
        Files.deleteIfExists(Paths.get(codeFilePath));

        if (outputExt != null) {
            String outputFilePath = Paths.get(OUTPUTS_DIRECTORY, String.format("%s.%s", uuid, outputExt)).toString();
            Files.deleteIfExists(Paths.get(outputFilePath));
        }
        if(lang.equals(("c")) || lang.equals("cpp")){

            String outputFilePath = Paths.get(OUTPUTS_DIRECTORY, String.format("%s", uuid)).toString();
            Files.deleteIfExists(Paths.get(outputFilePath));
        }
    }
}
