package pl.arekbednarz.todoapp.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.arekbednarz.todoapp.logic.ProjectService;
import pl.arekbednarz.todoapp.model.Project;
import pl.arekbednarz.todoapp.model.ProjectStep;
import pl.arekbednarz.todoapp.model.TaskRepository;
import pl.arekbednarz.todoapp.model.projection.ProjectWriteModel;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;
    private final TaskRepository repository;
    public ProjectController(ProjectService service, TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }


    @GetMapping
    String showProjects(Model model){
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping
    String addProject(
            @ModelAttribute("project") @Valid ProjectWriteModel current,
            BindingResult bindingResult,
            Model model
    )
    {
        if (bindingResult.hasErrors()){
            return "projects";
        }
        service.save(current);
        current.getSteps().forEach(repository::save);

        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Dodano projekt");
        model.addAttribute("projects", getProjects());
        return "projects";

    }


    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping("/{id}")
    String createGroup(
        @ModelAttribute("project") ProjectWriteModel current,
        Model model,
        @PathVariable int id,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
        ){
        try {
            service.createGroup(deadline, id);
            model.addAttribute("message","Dodano grupę");

        }catch (IllegalStateException| IllegalArgumentException e){
            model.addAttribute("message","Błąd podczas tworzenia grupy");
        }
        return "projects";
    }


    @ModelAttribute("projects")
    List<Project> getProjects(){
        return service.readAll();
    }


}
