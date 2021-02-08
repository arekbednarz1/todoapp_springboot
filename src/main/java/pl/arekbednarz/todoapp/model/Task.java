package pl.arekbednarz.todoapp.model;



import org.hibernate.annotations.Target;

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

    @Embedded
//    zmiana nazw atrybutow z audit
//    @AttributeOverrides
    private Audit audit= new Audit();

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;




//    @Transient mowi o tym ze nie chce miec pola w bazie


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

    public TaskGroup getGroup() {
        return group;
    }

    public void setGroup(TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(final Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;


    }

}
