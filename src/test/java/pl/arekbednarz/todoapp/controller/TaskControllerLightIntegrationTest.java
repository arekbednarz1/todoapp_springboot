package pl.arekbednarz.todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.arekbednarz.todoapp.model.Task;
import pl.arekbednarz.todoapp.model.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@WebMvcTest(TaskController.class)
@ActiveProfiles("integration")
public class TaskControllerLightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private TaskRepository repository;


    @Test
    void httpGet_returnsGivenTasks() throws Exception {
//        given
        String description = "foo";
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Task(description, LocalDateTime.now())));

//    when +then
    mockMvc.perform(MockMvcRequestBuilders.get("/tasks/123"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().string(containsString(description)));
    }
}
