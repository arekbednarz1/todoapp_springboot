package pl.arekbednarz.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.arekbednarz.todoapp.model.Project;
import pl.arekbednarz.todoapp.model.ProjectRepository;
import pl.arekbednarz.todoapp.model.TaskGroup;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project,Long> {

    @Override
    @Query("from Project p join fetch p.step")
    List<Project> findAll();
}
