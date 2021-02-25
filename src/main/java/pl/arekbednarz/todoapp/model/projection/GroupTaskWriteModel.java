package pl.arekbednarz.todoapp.model.projection;

import pl.arekbednarz.todoapp.model.Task;
import pl.arekbednarz.todoapp.model.TaskGroup;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class GroupTaskWriteModel {
    @NotBlank(message="Description must be not null")
    private String description;
    private LocalDateTime deadline;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask(TaskGroup result){
        return new Task(description, deadline, result);
    }
}
