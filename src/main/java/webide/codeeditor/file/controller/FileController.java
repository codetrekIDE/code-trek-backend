package webide.codeeditor.file.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webide.codeeditor.file.model.FileRequest;
import webide.codeeditor.file.model.FileResponse;
import webide.codeeditor.file.service.FileOperationService;

import java.io.IOException;

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

    @PutMapping("/update")
    public ResponseEntity<String> updateFile(@RequestBody FileRequest fileRequest) {
        try {
            dataFileService.updateFile(fileRequest.getPath(), fileRequest.getContent());
            return ResponseEntity.ok("File updated successfully at : " + fileRequest.getPath());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{path}")
    public ResponseEntity<String> deleteFile(@PathVariable String path) {
        try {
            dataFileService.deleteFile(path);
            return ResponseEntity.ok("File deleted successfully at : " + path);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/read/{path}")
    public ResponseEntity<String> readFile(@PathVariable String path) {
        try {
            String content = dataFileService.readFile(path);
            return ResponseEntity.ok("File read successfully at : " + path);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}