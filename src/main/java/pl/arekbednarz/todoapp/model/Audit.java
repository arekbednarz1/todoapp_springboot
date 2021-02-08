package pl.arekbednarz.todoapp.model;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;


//klasa bazowa ktora pozwala zamoddelowac na bazie

@Embeddable
public class Audit {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;


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
