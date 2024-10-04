package webide.codeeditor.file.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRequest {
    private String path;
    private String content;

    public FileRequest() {
    }

    public FileRequest(String path, String content) {
        this.path = path;
        this.content = content;
    }
}
