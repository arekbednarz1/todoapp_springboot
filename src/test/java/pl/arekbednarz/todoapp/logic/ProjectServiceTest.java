package pl.arekbednarz.todoapp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.arekbednarz.todoapp.TaskConfigurationProperties;
import pl.arekbednarz.todoapp.model.*;
import pl.arekbednarz.todoapp.model.projection.GroupReadModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw exception when configured to allow just 1 group and the other group undone")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExist_throwsIllegalStateException() {
//        given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
//        and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
//        system under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig, null);
//     when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
//        then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }


    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
//      given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
//        and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
//        system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig, null);
//        when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
//        then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }


    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just one group and no groups and no projects")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
//      given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //        and
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
//        and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
//        system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig,null);
//        when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
//        then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSaveGroup() {
//      given
        var today = LocalDate.now().atStartOfDay();
//        and
        var project =  projectWith("bartest", Set.of(-1,-2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
//and
        InMemoryGroupRepository inMemory = inMemoryGroupRepository();
        TaskGroupService serviceMemRepo = dummyGroupService(inMemory);
        int countBeforeCall = inMemoryGroupRepository().count();
//        and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
//        system under test
        var toTest = new ProjectService(mockRepository, inMemory, mockConfig, serviceMemRepo);
//        when
        GroupReadModel result = toTest.createGroup(today,1);
//        then
//        assertThat(result)
//                .hasFieldOrPropertyWithValue("description","bartest");
        assertThat(result.getDescription()).isEqualTo("bartest");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(countBeforeCall+ 1).isNotEqualTo(inMemoryGroupRepository().count());
    }

    private TaskGroupService dummyGroupService(final InMemoryGroupRepository inMemoryGroupRepository) {
         return new TaskGroupService(inMemoryGroupRepository,null);

    }

    private Project projectWith(String projectDescription, Set<Integer>daysToDeadline){
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        Set<ProjectStep> steps = daysToDeadline.stream().map(days->{
            var step = mock(ProjectStep.class);
            when(step.getDescription()).thenReturn("test");
            when(step.getDaysToDeadline()).thenReturn(days);
            return step;
        }).collect(Collectors.toSet());
        when(result.getStep()).thenReturn(steps);
        return result;
    }




    private InMemoryGroupRepository inMemoryGroupRepository() {
       return new InMemoryGroupRepository();
    }



    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private Map<Integer, TaskGroup> map = new HashMap<>();
        private int index = 0;

        public int count(){
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return map.values().stream().collect(Collectors.toList());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() != 0) {
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProjectId(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    };




    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProjectId(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }
}
