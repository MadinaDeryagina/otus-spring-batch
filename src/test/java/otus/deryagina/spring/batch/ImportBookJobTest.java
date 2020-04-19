package otus.deryagina.spring.batch;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static otus.deryagina.spring.batch.config.JobConfig.IMPORT_BOOK_JOB_NAME;

@Testcontainers
@SpringBatchTest
@Slf4j
@SpringBootTest
class ImportBookJobTest {


    @Container
    public static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.6.2")
            .withExposedPorts(9200).waitingFor(Wait.forHttp("/"));

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


    @BeforeAll
    static void setUp() {
        //elasticsearchContainer.start();
        String address = elasticsearchContainer.getContainerIpAddress() +":"+ elasticsearchContainer.getMappedPort(9200);
        log.info("===ADDRESS===: " + address);
        System.setProperty("spring.elasticsearch.rest.uris", address);

    }

    @Test
   void testJob() throws Exception {

        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_BOOK_JOB_NAME);

        JobParameters parameters = new JobParametersBuilder()
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

   }

}