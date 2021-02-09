package pl.arekbednarz.todoapp.logic;

import org.springframework.stereotype.Service;
import pl.arekbednarz.todoapp.TaskConfigurationProperties;
import pl.arekbednarz.todoapp.model.*;
import pl.arekbednarz.todoapp.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private TaskGroupRepository groupRepository;
    private TaskConfigurationProperties config;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository groupRepository, TaskConfigurationProperties config) {
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
        this.config = config;
    }


    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(Project toSave) {
        return projectRepository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (config.getTemplate().isAllowMultipleTasks() && groupRepository.existsByDoneIsFalseAndProjectId(projectId)) {
            throw new IllegalStateException("Tylko 1 nieskończona grupa w projekcie");
        }
        TaskGroup result = projectRepository.findById(projectId).map(project -> {
            var target = new TaskGroup();
            target.setDescription(project.getDescription());
            target.setTasks(project.getStep().stream()
                    .map(step -> new Task(step.getDescription(), deadline.plusDays(step.getDaysToDeadline()))
                    ).collect(Collectors.toSet())
            );
            return target;
        }).orElseThrow(() -> new IllegalArgumentException("Project z tym id nie znaleziona"));

        return new GroupReadModel(result);

    }
}
