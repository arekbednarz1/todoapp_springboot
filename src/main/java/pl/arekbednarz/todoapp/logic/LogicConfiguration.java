package pl.arekbednarz.todoapp.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.arekbednarz.todoapp.TaskConfigurationProperties;
import pl.arekbednarz.todoapp.model.ProjectRepository;
import pl.arekbednarz.todoapp.model.TaskGroupRepository;
import pl.arekbednarz.todoapp.model.TaskRepository;

@Configuration
class LogicConfiguration {



    @Bean
    ProjectService projectService(
           final ProjectRepository projectRepository,
           final TaskGroupRepository groupRepository,
           final TaskConfigurationProperties config){

        return new ProjectService(projectRepository,groupRepository,config);
    }

    @Bean
    TaskGroupService taskGroupService(
           final TaskGroupRepository repository,
           final TaskRepository taskRepository
    ){
        return new TaskGroupService(repository, taskRepository);
    }
}
