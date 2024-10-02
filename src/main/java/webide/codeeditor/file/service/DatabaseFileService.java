package webide.codeeditor.file.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class DatabaseFileService implements FileService {

    @Override
    public void createFile(String path, String content) throws IOException {
        Path filePath = getFilePath(path);
        if (Files.exists(filePath)) { // filePath에 경로가 이미 있는지
            throw new IOException("File already exists : " + path);
        }
        Files.write(filePath, content.getBytes()); // 경로를 불러오고, content를 저장
        log.info("File created at : {}", path);
    }

    @Override
    public void updateFile(String path, String content) throws IOException {
        Path filePath = getFilePath(path);
        if (!Files.exists(filePath)) { // 경로에 파일이 없으면 예외 터트리기
            throw new IOException("File not found : " + path);
        }
        Files.write(filePath, content.getBytes()); // 파일 덮어쓰기
        log.info("File updated at : {}", path);
    }

    @Override
    public void deleteFile(String path) throws IOException {
        Path filePath = getFilePath(path);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found : " + path);
        }
        Files.delete(filePath);
        log.info("File deleted at : {}", path);
    }

    @Override
    public String readFile(String path) throws IOException {
        Path filePath = getFilePath(path);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found : " + path);
        }
        String content = Files.readString(filePath); // 파일 내용을 읽음
        log.info("File read from : {}", path); // 읽기 후 출력
        return content; // 파일 내용 return
    }

    private static Path getFilePath(String path) {
        Path filePath = Paths.get(path); // string타입의 path를 filePath객체로 변환
        return filePath;
    }

}
