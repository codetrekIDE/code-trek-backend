package webide.codeeditor.file.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webide.codeeditor.file.model.PythonExecuteResponse;
import webide.codeeditor.file.model.PythonRequest;
import webide.codeeditor.file.service.CodeExecutionService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class PythonController {

    private final CodeExecutionService pythonExecutionService;

    public PythonController(CodeExecutionService pythonExecutionService) {
        this.pythonExecutionService = pythonExecutionService;
    }

    // Python 코드 실행 API
    @PostMapping("/execute")
    public ResponseEntity<?> executePythonCode(@RequestBody PythonRequest code) {
        try {
            String result = pythonExecutionService.executePythonCode(code.getCode());
            PythonExecuteResponse pythonExecuteResponse = new PythonExecuteResponse(result);
            return ResponseEntity.ok(pythonExecuteResponse);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error executing Python code: " + e.getMessage());
        }
    }
}
