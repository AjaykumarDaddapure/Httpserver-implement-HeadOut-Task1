package com.httpserver.HeadOutTask_1;

import spark.Spark;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        Spark.port(8080);

        Spark.get("/data", (req, res) -> {
            String fileName = req.queryParams("n");
            String lineNumberStr = req.queryParams("m");

            if (fileName == null || fileName.isEmpty()) {
                return "File name (n) is required.";
            }

            String filePath = "/tmp/data/" + fileName + ".txt";
            if (lineNumberStr != null && !lineNumberStr.isEmpty()) {
                try {
                    int lineNumber = Integer.parseInt(lineNumberStr);
                    return readLine(filePath, lineNumber);
                } catch (NumberFormatException e) {
                    return "Invalid line number.";
                }
            } else {
                return readFile(filePath);
            }
        });
    }

    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static String readLine(String filePath, int lineNumber) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int currentLine = 0;
            while ((line = reader.readLine()) != null) {
                currentLine++;
                if (currentLine == lineNumber) {
                    return line;
                }
            }
            return "Line not found.";
        }
    }
}

/*
 * Peoject Summary 
 * -------------- 
 * This project involved the implementation of a
 * simple HTTP server using the Spark Java framework, capable of responding to
 * GET requests on the "/data" endpoint. The server accepts query parameters 'n'
 * for the file name and 'm' for the line number, returning the content of the
 * specified file accordingly. Dockerization was applied to bundle the server
 * into an image compatible with both ARM and x86 architectures, with resource
 * constraints adhering to the project requirements. The implementation includes
 * thorough testing, documentation for building and running the project, and
 * suggestions for future improvements, making it a robust solution for a basic
 * HTTP server.
 * Note : I have note used any Image in the code.
 */







/*
 * mkdir target javac -cp "path/to/spark-2.9.3.jar" -d target FileServer.java
 * jar cvfe target/file-server.jar FileServer -C target .
 */

/*
 * docker build -t your-image-name .
 */

/*
 * docker run -p 8080:8080 --memory 1500m --cpus 2 your-image-name
 */