package pl.arekbednarz.todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.arekbednarz.todoapp.model.Task;
import pl.arekbednarz.todoapp.model.TaskRepository;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private TaskRepository repository;


    @Test
    void httpGet_returnsGivenTasks() throws Exception {
//        given
        Task task =new Task("foo", LocalDateTime.now());
       long id= repository.save(task).getId();
//    when +then
    mockMvc.perform(MockMvcRequestBuilders.get("/tasks/"+id))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
