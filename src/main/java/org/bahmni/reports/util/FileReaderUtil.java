package org.bahmni.reports.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileReaderUtil {
    private static final Logger logger = LogManager.getLogger(FileReaderUtil.class);

    public static String getFileContent(String relativePath) {
        try {
            Path path = Paths.get(FileReaderUtil.class.getClassLoader().getResource(relativePath).toURI());
            return Files.lines(path, StandardCharsets.UTF_8).collect(Collectors.joining("\n"));
        } catch (IOException | URISyntaxException | NullPointerException e) {
            logger.error("Error reading file at location {} - {}", relativePath, e);
            throw new RuntimeException(e);
        }
    }

    public static String getFileContent(String filePath, boolean isAbsolutePath) {
        if (!isAbsolutePath) {
            return getFileContent(filePath);
        }
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("File at location {} not found {}", filePath, e);
            throw new RuntimeException(e);
        }
    }
}
