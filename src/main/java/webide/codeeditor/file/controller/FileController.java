package webide.codeeditor.file.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webide.codeeditor.file.model.FileRequest;
import webide.codeeditor.file.model.FileResponse;
import webide.codeeditor.file.service.FileOperationService;

import java.io.IOException;
import java.util.UUID;

// API 컨트롤러
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileOperationService dataFileService;

    public FileController(FileOperationService dataFileService) {
        this.dataFileService = dataFileService;
    }

    // 파일 생성 API
    @PostMapping("/create")
    public ResponseEntity<String> createFile(@RequestBody FileRequest fileRequest) {
        try {
            dataFileService.createFile(fileRequest.getPath(), fileRequest.getContent());
            return ResponseEntity.ok("File created successfully at: " + fileRequest.getPath());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFile(@PathVariable UUID uuid, @RequestBody FileRequest fileRequest) {
        try {
            dataFileService.updateFileById(uuid, fileRequest.getContent());
            return ResponseEntity.ok("File updated successfully id : " + uuid);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable UUID uuid) {
        try {
            dataFileService.deleteFileById(uuid);
            return ResponseEntity.ok("File deleted successfully id : " + uuid);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/read/{path}")
    public ResponseEntity<String> readFile(@PathVariable UUID uuid) {
        try {
            String content = dataFileService.readFindById(uuid);
            return ResponseEntity.ok("File read successfully id : " + uuid);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}