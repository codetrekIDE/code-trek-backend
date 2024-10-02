package webide.codeeditor.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webide.codeeditor.file.model.FileResponse;
import webide.codeeditor.file.service.DatabaseFileService;

import java.io.IOException;

// API 컨트롤러
@RestController
@RequestMapping("/files")
public class FileController {

    private final DatabaseFileService dataFileService;

    public FileController(DatabaseFileService dataFileService) {
        this.dataFileService = dataFileService;
    }

    // 파일 생성 API
    @PostMapping("/create")
    public ResponseEntity<String> createFile(@RequestBody FileResponse fileResponse) {
        try {
            dataFileService.createFile(fileResponse.getPath(), fileResponse.getContent());
            return ResponseEntity.ok("File created successfully at: " + fileResponse.getPath());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateFile(@RequestParam String path, @RequestParam String content) {
        try {
            dataFileService.updateFile(path, content);
            return ResponseEntity.ok("File updated successfully at : " + path);
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