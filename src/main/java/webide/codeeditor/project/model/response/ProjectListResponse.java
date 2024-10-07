package webide.codeeditor.project.model.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProjectListResponse {
    private List<Long> projectIdList;
}
