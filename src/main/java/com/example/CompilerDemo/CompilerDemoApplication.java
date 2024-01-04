package com.example.CompilerDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CompilerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompilerDemoApplication.class, args);
	}

//
//	public static void compileCProgram() throws IOException, InterruptedException {
//		String fileName = "com/example/CompilerDemo/Demo1/example.c";
//		String outputFileName = "example";
//
//		// Compile the C program
//		ProcessBuilder processBuilder = new ProcessBuilder("gcc", fileName, "-o", outputFileName);
//		Process process = processBuilder.start();
//		process.waitFor();
//	}
//	public static void runCProgram() throws IOException, InterruptedException {
//		String fullPath = "/home/divum/Downloads/CompilerDemo/example";
//
//		// Run the compiled C program
//		ProcessBuilder processBuilder = new ProcessBuilder(fullPath);
//		Process process = processBuilder.start();
//		process.waitFor();
//	}
//
//
//	public static void main(String[] args) {
//		try {
//			compileCProgram();
//			System.out.println("C program compiled successfully!");
//
//			runCProgram();
//			System.out.println("C program executed successfully!");
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

}
