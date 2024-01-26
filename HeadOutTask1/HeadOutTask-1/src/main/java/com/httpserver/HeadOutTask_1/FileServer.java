package com.httpserver.HeadOutTask_1;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileServer {
    public static void main(String[] args) {
        Spark.port(8080);

        Spark.get("/data", new Route() {
            public Object handle(Request req, Response res) throws Exception {
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
