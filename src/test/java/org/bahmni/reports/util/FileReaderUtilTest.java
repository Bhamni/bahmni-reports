package org.bahmni.reports.util;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class FileReaderUtilTest {

    @BeforeClass
    public static void setup() throws IOException {
        createTestFile("./src/test/resources/sql/sampleSqlFile.sql", "This is a sample sql file");
        createTestFile("./src/test/sql/test.sql", "select * from someTable\nwhere a > 10");
    }

    @Test
    public void shouldReadFileContent() {
        String content = FileReaderUtil.getFileContent("sql/sampleSqlFile.sql");
        assertTrue(content.contains("This is a sample sql file"));

        content = FileReaderUtil.getFileContent("sql/sampleSqlFile.sql", false);
        assertTrue(content.contains("This is a sample sql file"));
    }

    @Test
    public void shouldReadFileContentFromAbsoluteFilePath() {
        String content = FileReaderUtil.getFileContent("./src/test/sql/test.sql", true);
        assertTrue(content.contains("select * from someTable"));
        assertTrue(content.contains("where a > 10"));
    }

    private static void createTestFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}
