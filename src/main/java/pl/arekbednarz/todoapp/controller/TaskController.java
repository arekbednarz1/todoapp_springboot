package pl.arekbednarz.todoapp.controller;


import jdk.jfr.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.arekbednarz.todoapp.model.Task;
import pl.arekbednarz.todoapp.model.TaskRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

//@RepositoryRestController
@RestController
//@Controller
//@RequestMapping(path = "/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskRepository repository;

    @Autowired
//    Autowired nie trzeba do wstrzykiwania
    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/tasks")
//    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate){
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }


//    @RequestMapping(method = RequestMethod.GET, path ="/tasks")
//    albo tak
//    params znaczy ze wchodzimy w ten kontroler tylko wtedy gdy nie ma tych parametrow

        @GetMapping(value = "/tasks", params = {"!sort","!page","!size"})
//    @RequestMapping(method = RequestMethod.GET, params = {"!sort","!page","!size"})
    ResponseEntity<List<Task>> readAllTasks(){
//    zamiennie z tym
//    List<Task> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
//        return repository.findAll();
    }

//    pageable to obiekt parametr stronicowania
    @GetMapping("/tasks")
//    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/tasks/{id}")
//    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    ResponseEntity<Task>readTask(@PathVariable long id){
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }



    @PutMapping("/tasks/{id}")
//    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    ResponseEntity<?> updateTask(@PathVariable long id, @RequestBody @Valid Task toUpdate){
        if (!repository.existsById(id)){
            ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }



//    transakcja musi byc na publicznej metodzie. ta metoda zmienia boolean true false czyli aktualizuje
    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable long id){
        if (!repository.existsById(id)){
            ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }
}



