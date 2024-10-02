package webide.codeeditor.file.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 응답 데이터 모델
public class FileResponse {
    private String path;
    private String content;

    public FileResponse() {
    }

    public FileResponse(String path, String content) {
        this.path = path;
        this.content = content;
    }
}
