package pl.arekbednarz.todoapp.logic;

import org.springframework.stereotype.Service;
import pl.arekbednarz.todoapp.TaskConfigurationProperties;
import pl.arekbednarz.todoapp.model.*;
import pl.arekbednarz.todoapp.model.projection.GroupReadModel;
import pl.arekbednarz.todoapp.model.projection.GroupTaskWriteModel;
import pl.arekbednarz.todoapp.model.projection.GroupWriteModel;
import pl.arekbednarz.todoapp.model.projection.ProjectWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service nie potrzeba kiedy utworzylem logic configuration
public class ProjectService {

    private ProjectRepository projectRepository;
    private TaskGroupRepository groupRepository;
    private TaskConfigurationProperties config;
    private TaskGroupService service;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository groupRepository, TaskConfigurationProperties config, TaskGroupService service) {
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
        this.config = config;
        this.service = service;
    }


    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }


    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && groupRepository.existsByDoneIsFalseAndProjectId(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getStep().stream()
                                    .map(step -> {
                                        var task = new GroupTaskWriteModel();
                                               task.setDescription(step.getDescription());
                                               task.setDeadline( deadline.plusDays(step.getDaysToDeadline()));
                                               return task;
                                    }
                                    ).collect(Collectors.toSet())
            );
                   return service.createGroup(targetGroup,project);
        }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));


    }
}
