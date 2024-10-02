package webide.codeeditor.file.service;

import java.io.IOException;

public interface FileService {
    void createFile(String path, String content) throws IOException;

    void updateFile(String path, String content) throws IOException;

    void deleteFile(String path) throws IOException;

    String readFile(String path) throws IOException;
}
