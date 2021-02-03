package pl.arekbednarz.todoapp;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
public class TaskConfigurationProperties {
//    private boolean allowMultipleTasksFromTemplate;
//
//    public boolean isAllowMultipleTasksFromTemplate() {
//        return allowMultipleTasksFromTemplate;
//    }
//
//    public void setAllowMultipleTasksFromTemplate(boolean allowMultipleTasksFromTemplate) {
//        this.allowMultipleTasksFromTemplate = allowMultipleTasksFromTemplate;
//    }

    private Template template;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(final Template template) {
        this.template = template;
    }


    public static class Template {
        private boolean allowMultipleTasks;

        public boolean isAllowMultipleTasks() {
            return allowMultipleTasks;
        }

        public void setAllowMultipleTasks(boolean allowMultipleTasks) {
            this.allowMultipleTasks = allowMultipleTasks;
        }
    }
}
