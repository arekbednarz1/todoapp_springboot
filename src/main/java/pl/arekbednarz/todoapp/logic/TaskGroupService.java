package pl.arekbednarz.todoapp.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.RequestScope;
import pl.arekbednarz.todoapp.TaskConfigurationProperties;
import pl.arekbednarz.todoapp.model.Task;
import pl.arekbednarz.todoapp.model.TaskGroup;
import pl.arekbednarz.todoapp.model.TaskGroupRepository;
import pl.arekbednarz.todoapp.model.TaskRepository;
import pl.arekbednarz.todoapp.model.projection.GroupReadModel;
import pl.arekbednarz.todoapp.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;
//scope mowi jak obiekt powinien byc wstrzykiwany np wzorce projektowe
//scope request uzywamy aby w obrebie jednego żądania mieć unikalną instancje serwisu
//scope aplication servlet context
@Service
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//@RequestScope
//@ApplicationScope
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskConfigurationProperties config;
    private TaskRepository taskRepository;


    public TaskGroupService(TaskGroupRepository repository, TaskConfigurationProperties config, TaskRepository taskRepository) {
        this.repository = repository;
        this.config = config;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroupId(groupId)) {
            throw new IllegalStateException("Nie mozna wykonac dopoki jest niewykoany task");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup z tym id nie znaleziono"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}

