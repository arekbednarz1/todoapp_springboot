package pl.arekbednarz.todoapp.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@RepositoryRestResource
//@RepositoryRestResource(path = "todos", collectionResourceRel = "todos")
@Repository
interface SqlTaskRepository extends TaskRepository,JpaRepository<Task,Long> {


//    rest resource powoduje ze nie ma dostepu do metody na zewnatrz


//    jak jest repository to te metody nie trzeba wypisac
//    @Override
//    @RestResource(exported = false)
//    void deleteById(Long aLong);
//
//    @Override
//    @RestResource(exported = false)
//    void delete(Task task);

//    @RestResource(path = "done", rel="done")
//    List<Task> findByDoneIsTrue();

//    @RestResource(path = "done", rel="done")
//    List<Task> findByDone(@Param("state")boolean done);
}
