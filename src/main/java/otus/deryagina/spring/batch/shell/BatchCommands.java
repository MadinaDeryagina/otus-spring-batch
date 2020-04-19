package otus.deryagina.spring.batch.shell;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;


@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final Job importBookJob;

    private final JobLauncher jobLauncher;


    @SneakyThrows
    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "sm-jl")
    public void startMigrationJobWithJobLauncher() {
        JobExecution execution = jobLauncher.run(importBookJob, new JobParametersBuilder()
                .toJobParameters());
        System.out.println(execution);
    }

}
