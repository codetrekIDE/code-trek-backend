package webide.codeeditor.file.service; // FileOperationService.java 파일 상단에 있어야 함

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import webide.codeeditor.file.repository.FileEntity;
import webide.codeeditor.file.repository.FileRepository;
import webide.codeeditor.project.repository.Project;
import webide.codeeditor.project.repository.ProjectRepository;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j // Lombok의 로깅 기능 사용
public class FileOperationService implements FileService {

    private final FileRepository fileRepository; // 의존성 주입을 위한 리포지토리
    // 프로젝트 기능 위해 추가
    private final ProjectRepository projectRepository;

    // 생성자를 통해 FileRepository 주입
    public FileOperationService(FileRepository fileRepository, ProjectRepository projectRepository) {
        this.fileRepository = fileRepository;

        // 프로젝트 기능 위해 추가
        this.projectRepository = projectRepository;
    }

    //id기반으로 생성
    @Override
    public void createFile(String path, String content) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) {
            throw new IOException("File already exists: " + path);
        }
        Files.write(filePath, content.getBytes());

        // UUID는 서버에서 생성하여 관리
        FileEntity fileEntity = new FileEntity(path, content); // UUID는 자동 생성
        fileRepository.save(fileEntity);

        log.info("File created at: {}, UUID : {}", filePath, fileEntity.getId()); // 로그 출력
    }

    //id기반으로 생성
    //프로젝트 기능 위해 추가
    public void createFile(String path, String content, Long projectId) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) {
            throw new IOException("File already exists: " + path);
        }
        Files.write(filePath, content.getBytes());

        // UUID는 서버에서 생성하여 관리
        FileEntity fileEntity = new FileEntity(path, content); // UUID는 자동 생성

        Optional<Project> projectOptional = projectRepository.findById(projectId);
        Project project = projectOptional.get();
        fileEntity.setProject(project);

        fileRepository.save(fileEntity);
        Files.deleteIfExists(Paths.get(path));

        log.info("File created at: {}, UUID : {}", filePath, fileEntity.getId()); // 로그 출력
    }

    public String readFindById(UUID id) throws IOException {
        //UUID로 파일을 조회
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(id);
        if (!fileEntityOptional.isPresent()) {
            throw new IOException("File not found in database ID : " + id);
        }

        FileEntity fileEntity = fileEntityOptional.get();

        //파일 시스템에서 파일 내용 읽기
        Path filePath = Paths.get(fileEntity.getFileName());
        if (!Files.exists(filePath)) {
            throw new IOException("File not found files" + fileEntity.getFileName());
        }

        log.info("File read from database UUID: {}", fileEntity.getFileName(), id);
        return fileEntity.getContent();
    }

    // UUID로 파일 삭제 메서드 추가
    public void deleteFileById(UUID id) throws IOException {
        // UUID로 데이터베이스에서 파일 조회
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(id);
        if (!fileEntityOptional.isPresent()) {
            throw new IOException("File not found in database with ID: " + id);
        }
        FileEntity fileEntity = fileEntityOptional.get();

        // 파일 시스템에서 파일 삭제
        Path filePath = Paths.get(fileEntity.getFileName());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("File deleted from filesystem: {}", filePath);
        } else {
            log.warn("File not found in filesystem for deletion: {}", filePath);
        }

        // 데이터베이스에서 파일 엔티티 삭제
        fileRepository.delete(fileEntity);
        log.info("File deleted from database: {} with UUID: {}", fileEntity.getFileName(), id);
    }

    // UUID로 파일 업데이트 메서드 추가
    public void updateFileById(UUID id, String content) throws IOException {
        // UUID로 데이터베이스에서 파일 조회
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(id);
        if (!fileEntityOptional.isPresent()) {
            throw new IOException("File not found in database with ID: " + id);
        }
        FileEntity fileEntity = fileEntityOptional.get();

        // 파일 시스템에서 파일 내용 업데이트
        Path filePath = Paths.get(fileEntity.getFileName());
        if (!Files.exists(filePath)) {
            throw new IOException("File not found in filesystem: " + fileEntity.getFileName());
        }

        // 파일 내용 덮어쓰기
        Files.write(filePath, content.getBytes());
        log.info("File content updated in filesystem: {}", filePath);

        // 데이터베이스에 있는 파일 내용도 업데이트
        fileEntity.setContent(content);
        fileRepository.save(fileEntity);
        log.info("File updated in database: {} with UUID: {}", fileEntity.getFileName(), id);
    }

    @Override
    public String readFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + path);
        }

        // 파일 내용을 데이터베이스에서 조회
        Optional<FileEntity> fileEntityOptional = fileRepository.findByFileName(path);

        // 파일이 존재하지 않을 경우 예외 처리
        if (!fileEntityOptional.isPresent()) {
            throw new IOException("File not found in database");
        }

        FileEntity fileEntity = fileEntityOptional.get(); // 파일 엔티티 추출
        log.info("File read from database: {}", fileEntity.getFileName());
        return fileEntity.getContent(); // 파일 내용 반환
    }

    @Override
    public void deleteFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + path);
        }

        // 파일 시스템에서 파일 삭제
        Files.delete(filePath);
        log.info("File deleted from filesystem: {}", filePath);

        // 데이터베이스에서 파일 엔티티 삭제
        Optional<FileEntity> fileEntityOptional = fileRepository.findByFileName(path);

        // 파일이 존재하지 않을 경우 예외 처리
        if (!fileEntityOptional.isPresent()) {
            throw new IOException("File not found in database");
        }

        FileEntity fileEntity = fileEntityOptional.get(); // 파일 엔티티 추출
        fileRepository.delete(fileEntity); // 파일 엔티티 삭제
        log.info("File deleted from database: {}", fileEntity.getFileName());
    }

    @Override
    public void updateFile(String path, String content) throws IOException {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + path);
        }

        // 파일 내용을 덮어쓰기
        Files.write(filePath, content.getBytes());

        // 데이터베이스에 있는 파일 내용도 업데이트
        Optional<FileEntity> fileEntityOptional = fileRepository.findByFileName(path);

        // 파일이 존재하지 않을 경우 예외 처리
        if (!fileEntityOptional.isPresent()) {
            throw new IOException("File not found in database");
        }

        FileEntity fileEntity = fileEntityOptional.get(); // 파일 엔티티 추출
        fileEntity.setContent(content); // 파일 내용 업데이트
        fileRepository.save(fileEntity); // 데이터베이스에 업데이트된 파일 저장
        log.info("File updated at: {}", filePath);
    }
}
