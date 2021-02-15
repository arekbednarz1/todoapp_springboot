package pl.arekbednarz.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import pl.arekbednarz.todoapp.logic.ProjectService;

@EnableAsync
@SpringBootApplication
//@ComponentScan(basePackages = "db.migration") inne pakiety do skanowania
//@Import(ProjectService.class) to samo
public class TodoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
    }

    @Bean
    LocalValidatorFactoryBean validator(){
        return new LocalValidatorFactoryBean();
    }


//    @Override
//    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
//        validatingListener.addValidator("beforeCreate",validator());
//        validatingListener.addValidator("beforeSave",validator());
//
//    }
}

