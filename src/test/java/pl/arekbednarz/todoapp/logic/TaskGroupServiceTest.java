package pl.arekbednarz.todoapp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.arekbednarz.todoapp.TaskConfigurationProperties;
import pl.arekbednarz.todoapp.model.TaskGroup;
import pl.arekbednarz.todoapp.model.TaskGroupRepository;
import pl.arekbednarz.todoapp.model.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw exception when group has a undone task")
    void toggleGroup_when_group_has_undoneTask_throwsIllegalStateException() {
//        given
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);
//        system under test
        var toTest = new TaskGroupService(null,mockTaskRepository);
        var exception = catchThrowable(()->toTest.toggleGroup(1));

//        then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone task");
    }

    private TaskRepository taskRepositoryReturning(final boolean result) {
        var mockTaskRepository= mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroupId(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }

    @Test
    @DisplayName("should throw exception when not found task group with given id")
    void toggleGroup_when_taskGroup_with_givenId_notFound_throwIllegalArgumentException(){
//        given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
//        and
        var mockRepositoryTaskGroup = mock(TaskGroupRepository.class);
        when(mockRepositoryTaskGroup.findById(anyInt())).thenReturn(Optional.empty());
//        system under test
        var toTest = new TaskGroupService(mockRepositoryTaskGroup,mockTaskRepository);

//        when
        var exception = catchThrowable(()->toTest.toggleGroup(1));

//        them
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");
    }



    @Test
    @DisplayName("should toggle group")
    void toggleGroup_workAll(){
//        given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
//        and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        var mockRepositoryTaskGroup = mock(TaskGroupRepository.class);
        when(mockRepositoryTaskGroup.findById(anyInt())).thenReturn(Optional.of(group));
//        system under test
        var toTest = new TaskGroupService(mockRepositoryTaskGroup,mockTaskRepository);

//        when
       toTest.toggleGroup(0);

//        them
        assertThat(group.isDone()).isEqualTo(!beforeToggle);

    }
}