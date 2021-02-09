package pl.arekbednarz.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.arekbednarz.todoapp.model.TaskGroup;
import pl.arekbednarz.todoapp.model.TaskGroupRepository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup,Long> {

    @Override
//    zapytanie olewa lazy loading kiedy czyta grupe pobiera od razu wszystkie taski dla tej grupy
//    zzapytanie HQL
    @Query("select distinct g from TaskGroup g join fetch g.tasks")
    List<TaskGroup>findAll();

    @Override
    boolean existsByDoneIsFalseAndProjectId(Integer projectId);
}
