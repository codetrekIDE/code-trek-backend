package webide.codeeditor.file.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PythonRequest {
    private String path;
    private String content;

    public PythonRequest() {
    }

    public PythonRequest(String path, String content) {
        this.path = path;
        this.content = content;
    }
}
