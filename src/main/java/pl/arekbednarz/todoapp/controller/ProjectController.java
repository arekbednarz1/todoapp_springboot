package pl.arekbednarz.todoapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.arekbednarz.todoapp.model.projection.ProjectWriteModel;

@Controller
@RequestMapping("/projects")
public class ProjectController {


    @GetMapping
    String showProjects(Model model){
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }



}
