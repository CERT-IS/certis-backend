package certis.CertisHomepage.service;

import certis.CertisHomepage.domain.entity.ProjectEntity;
import certis.CertisHomepage.repository.ProjectRepository;
import certis.CertisHomepage.domain.dto.project.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;


    //전체 프로젝트 찾기
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjects(){
        List<ProjectEntity> projects = projectRepository.findAll();
        List<ProjectDto> projectDtos = new ArrayList<>();
        projects.forEach(t -> projectDtos.add(ProjectDto.toDto(t)));

        return projectDtos;
    }


    //id값으로 프로젝트 찾기
    @Transactional(readOnly = true)
    public ProjectDto getProject(Long id){
        ProjectEntity project = projectRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Project Id를 찾을 수없음");
        });

        ProjectDto dto = ProjectDto.toDto(project);
        return dto;
    }


    public ProjectEntity register(ProjectDto projectDto){

        ProjectEntity project = ProjectEntity.builder()
                .writer(projectDto.getWriter())
                .projectName(projectDto.getProjectName())
                .content(projectDto.getContent())
                .user(projectDto.getUser())
                .modifiedAt(LocalDateTime.now())
                .registeredAt(LocalDateTime.now())
                .build();

        return project;
    }








}
