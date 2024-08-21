package certis.CertisHomepage.repository;

import certis.CertisHomepage.domain.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    ProjectEntity findByProjectName(String projectName);

}
