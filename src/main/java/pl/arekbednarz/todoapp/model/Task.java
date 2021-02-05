package pl.arekbednarz.todoapp.model;



import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="Description must be not null")
    private String description;
    private boolean done;
    private LocalDateTime deadline;

//    @Transient mowi o tym ze nie chce miec pola w bazie
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;


    public Task() {
    }

    public Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void updateFrom(final Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline;


    }

//    adnotacja mowi ze to ma byc wykonane przed zapisaniem do bazy

    @PrePersist
    void prePersist(){
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preMerge(){
        updatedOn = LocalDateTime.now();
    }

}
