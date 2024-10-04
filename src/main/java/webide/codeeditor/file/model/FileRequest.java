package webide.codeeditor.file.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FileRequest {
    private  String id;
    private String path;
    private String content;

    public FileRequest() {
        this.id = UUID.randomUUID().toString();
    }

    public FileRequest(String path, String content) {
        this.id = UUID.randomUUID().toString();
        this.path = path;
        this.content = content;
    }
}
