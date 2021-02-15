package pl.arekbednarz.todoapp.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.arekbednarz.todoapp.logic.TaskService;
import pl.arekbednarz.todoapp.model.Task;
import pl.arekbednarz.todoapp.model.TaskRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;

    @Autowired
     TaskController(TaskRepository repository, TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate){
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }


    @GetMapping(params = {"!sort","!page","!size"})
   CompletableFuture<ResponseEntity<List<Task>>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return service.findAllAsunc().thenApply(ResponseEntity::ok);
    }

//    pageable to obiekt parametr stronicowania
    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/{id}")
//    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    ResponseEntity<Task>readTask(@PathVariable long id){
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state){
        return ResponseEntity.ok(
                repository.findByDone(state)
        );


    }



    @PutMapping("/{id}")
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
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable long id){
        if (!repository.existsById(id)){
            ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }
}



