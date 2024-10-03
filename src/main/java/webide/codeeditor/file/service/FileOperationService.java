package webide.codeeditor.file.service; // FileOperationService.java 파일 상단에 있어야 함

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import webide.codeeditor.file.repository.FileEntity;
import webide.codeeditor.file.repository.FileRepository;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@Slf4j // Lombok의 로깅 기능 사용
public class FileOperationService implements FileService {

    private final FileRepository fileRepository; // 의존성 주입을 위한 리포지토리

    // 생성자를 통해 FileRepository 주입
    public FileOperationService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void createFile(String path, String content) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) {
            throw new IOException("File already exists: " + path);
        }
        Files.write(filePath, content.getBytes());

        // 파일 엔티티 생성 후 데이터베이스에 저장
        FileEntity fileEntity = new FileEntity(path, content);
        fileRepository.save(fileEntity);

        log.info("File created at: {}", filePath); // 로그 출력
    }

    @Override
    public String readFile(String path) throws IOException {
        String filePath = Paths.get(path, "main.py").toString();
        if (!Files.exists(Paths.get(filePath))) {
            throw new IOException("File not found: " + path);
        }

        // 파일 내용을 데이터베이스에서 조회
        Optional<FileEntity> fileEntityOptional = fileRepository.findByFileName("main.py");
        FileEntity fileEntity = fileEntityOptional.orElseThrow(() -> new IOException("File not found in database"));
        log.info("File read from database: {}", fileEntity.getFileName());
        return fileEntity.getContent(); // 파일 내용 반환
    }

    @Override
    public void deleteFile(String path) throws IOException {
        String filePath = Paths.get(path, "main.py").toString();
        if (!Files.exists(Paths.get(filePath))) {
            throw new IOException("File not found: " + path);
        }

        // 파일 시스템에서 파일 삭제
        Files.delete(Paths.get(filePath));
        log.info("File deleted from filesystem: {}", filePath);

        // 데이터베이스에서 파일 엔티티 삭제
        Optional<FileEntity> fileEntityOptional = fileRepository.findByFileName("main.py");
        FileEntity fileEntity = fileEntityOptional.orElseThrow(() -> new IOException("File not found in database"));
        fileRepository.delete(fileEntity);
        log.info("File deleted from database: {}", fileEntity.getFileName());
    }

    @Override
    public void updateFile(String path, String content) throws IOException {
        String filePath = Paths.get(path, "main.py").toString();
        if (!Files.exists(Paths.get(filePath))) {
            throw new IOException("File not found: " + path);
        }

        // 파일 내용을 덮어쓰기
        Files.write(Paths.get(filePath), content.getBytes());

        // 데이터베이스에 있는 파일 내용도 업데이트
        Optional<FileEntity> fileEntityOptional = fileRepository.findByFileName("main.py");
        FileEntity fileEntity = fileEntityOptional.orElseThrow(() -> new IOException("File not found in database"));
        fileEntity.setContent(content);
        fileRepository.save(fileEntity);
        log.info("File updated at: {}", filePath);
    }
}
