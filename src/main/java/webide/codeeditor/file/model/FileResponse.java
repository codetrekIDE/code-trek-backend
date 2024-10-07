package webide.codeeditor.file.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 응답 데이터 모델
public class FileResponse {
    private String id;
    private String path;
    private String content;
    private String message; //파일 작업에 대한 결과 메시지 ex) File created successfully

    public FileResponse() {
    }

    public FileResponse(String id, String path, String content, String message) {
        this.id = id;
        this.path = path;
        this.message = message;
        this.content = content;
    }
}
