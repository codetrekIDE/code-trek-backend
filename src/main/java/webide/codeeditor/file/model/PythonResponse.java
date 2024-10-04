package webide.codeeditor.file.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PythonResponse {
    private String output;
    private long executionTime;

    public PythonResponse() {
    }

    public PythonResponse(String output, long executionTime) {
        this.output = output;
        this.executionTime = executionTime;
    }
}
