package webide.codeeditor.file.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PythonRequest {
    private String code;

    public PythonRequest() {
    }

    public PythonRequest(String code) {
        this.code = code;
    }
}
