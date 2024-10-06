package webide.codeeditor.project.model.response;


import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;

}
