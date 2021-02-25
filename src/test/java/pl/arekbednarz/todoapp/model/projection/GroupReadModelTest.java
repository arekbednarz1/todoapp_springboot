package pl.arekbednarz.todoapp.model.projection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.arekbednarz.todoapp.model.Task;
import pl.arekbednarz.todoapp.model.TaskGroup;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GroupReadModelTest {

    @Test
    @DisplayName("should create deadline null for group when no tasks deadlines")
   void constructor_noDeadline_createsNullDeadline(){
//        given
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar",null)));

//        when
        var result = new GroupReadModel(source);

//        then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}