package pl.arekbednarz.todoapp.model.projection;

import org.springframework.beans.factory.annotation.Autowired;
import pl.arekbednarz.todoapp.model.Project;
import pl.arekbednarz.todoapp.model.ProjectStep;
import pl.arekbednarz.todoapp.model.TaskRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectWriteModel {

    @NotBlank(message = "Project description must be not null")
    private String description;

    @Valid
    private List<ProjectStep> steps = new ArrayList<>();

    public ProjectWriteModel(){
        steps.add(new ProjectStep());
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ProjectStep> steps) {
        this.steps = steps;
    }

    public Project toProject(){
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        Set<ProjectStep>step=new HashSet<>();
        step.addAll(steps);
        result.setStep(step);
        return result;
    }

}
