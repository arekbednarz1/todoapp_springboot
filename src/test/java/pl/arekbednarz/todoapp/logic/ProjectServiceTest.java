package pl.arekbednarz.todoapp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.arekbednarz.todoapp.TaskConfigurationProperties;
import pl.arekbednarz.todoapp.model.TaskGroupRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throwv exception when configurated to allow just 1 grpup and the other group undone")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExist_throwsIllegalStateException() {
//        miejsce w ktorym przygotowujemy dane given
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProjectId(1)).thenReturn(true);
//        and
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(false);
//        and
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
//        sytem under test
        var toTest = new ProjectService(null,mockGroupRepository,mockConfig);
//     when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(),0));
//        then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");



    }
}