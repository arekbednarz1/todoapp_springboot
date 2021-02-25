package pl.arekbednarz.todoapp.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.arekbednarz.todoapp.logic.TaskGroupService;
import pl.arekbednarz.todoapp.model.ProjectStep;
import pl.arekbednarz.todoapp.model.Task;
import pl.arekbednarz.todoapp.model.TaskRepository;
import pl.arekbednarz.todoapp.model.projection.GroupReadModel;
import pl.arekbednarz.todoapp.model.projection.GroupTaskWriteModel;
import pl.arekbednarz.todoapp.model.projection.GroupWriteModel;
import pl.arekbednarz.todoapp.model.projection.ProjectWriteModel;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@Controller
@RequestMapping("/groups")
 class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService service;
    private final TaskRepository repository;

    TaskGroupController(TaskGroupService service, TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model){
        model.addAttribute("group",new GroupWriteModel());
        return "groups";
    }


    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate){
        GroupReadModel group = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/"+group.getId())).body(group);
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readAllGroups(){
        return ResponseEntity.ok(service.readAll());
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>>readAllTaskFromGroup(@PathVariable int id){
        return ResponseEntity.ok(repository.findAllByGroupId(id));
    }


    @ResponseBody
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id){
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ModelAttribute("groups")
    private List<GroupReadModel> getGroups() {
        return service.readAll();
    }



    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(
            @ModelAttribute("project") @Valid GroupWriteModel current,
            BindingResult bindingResult,
            Model model
    )
    {
        if (bindingResult.hasErrors()){
            return "groups";
        }
        service.createGroup(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Dodano grupe");
        model.addAttribute("projects", getGroups());
        return "groups";

    }



    @PostMapping(params = "addTasks",produces = MediaType.TEXT_HTML_VALUE)
    String addGroupTasks(@ModelAttribute("group") GroupWriteModel current){
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }
}



