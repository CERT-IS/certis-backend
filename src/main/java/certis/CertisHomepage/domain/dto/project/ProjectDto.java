package certis.CertisHomepage.domain.dto.project;


import certis.CertisHomepage.domain.entity.ProjectEntity;
import certis.CertisHomepage.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private String projectName;

    private String content;

    private String writer;

    private List<UserEntity> user;

    public static ProjectDto toDto(ProjectEntity project){
        return new ProjectDto(project.getProjectName(), project.getContent(), project.getWriter(), project.getUser());
    }

}
