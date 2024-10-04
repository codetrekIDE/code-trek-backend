package webide.codeeditor.file.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webide.codeeditor.file.model.FileResponse;
import webide.codeeditor.file.model.PythonRequest;
import webide.codeeditor.file.service.CodeExecutionService;

import java.io.IOException;

@RestController
@RequestMapping("/execute")
public class PythonController {

    private final CodeExecutionService pythonExecutionService;

    public PythonController(CodeExecutionService pythonExecutionService) {
        this.pythonExecutionService = pythonExecutionService;
    }

    // Python 코드 실행 API
    @PostMapping("/python")
    public ResponseEntity<String> executePythonCode(@RequestBody FileResponse code) {
        try {
            String result = pythonExecutionService.executePythonCode(code.getContent());
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error executing Python code: " + e.getMessage());
        }
    }
}
