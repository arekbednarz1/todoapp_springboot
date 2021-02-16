package pl.arekbednarz.todoapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.arekbednarz.todoapp.model.Task;
import pl.arekbednarz.todoapp.model.TaskGroup;
import pl.arekbednarz.todoapp.model.TaskRepository;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;

@Configuration
 class TestConfiguration {

    @Bean
    @Primary
    @Profile("!integration")
    DataSource e2eTestDataSource(){
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1","sa","");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }





//    repozytorium symulujące baze danych przechowywane w pamieci
    @Bean
    @Primary
    @Profile("integration")
    TaskRepository testRepo(){
        return new TaskRepository() {
            private Map<Long, Task> tasks= new HashMap<>();

            @Override
            public List<Task> findAll() {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Optional<Task> findById(Long id) {
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public Task save(Task entity) {
                long key = tasks.size()+1;
                try {
                   var field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity,key);

                } catch (NoSuchFieldException | IllegalAccessException e){
                    throw new RuntimeException(e);
                }
                tasks.put(key,entity);
                return tasks.get(key);
            }

            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public List<Task> findByDone(boolean done) {
                return null;
            }

            @Override
            public boolean existsById(Long id) {
                return tasks.containsKey(id);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroupId(Integer groupId) {
                return false;
            }

            @Override
            public List<Task> findAllByGroupId(Integer groupId) {
                return List.of();
            }
        };
    }
}
