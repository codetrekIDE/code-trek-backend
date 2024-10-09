package webide.codeeditor.project.model.response;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
public class ProjectListResponse {

    private Long id;
    private String title;
    private Timestamp updated_at;

}
