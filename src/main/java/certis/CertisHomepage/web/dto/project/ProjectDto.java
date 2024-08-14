/*
package certis.CertisHomepage.web.dto.project;



import certis.CertisHomepage.domain.UserEntity;
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
*/
