package otus.deryagina.spring.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import otus.deryagina.spring.batch.nosql.dao.BookDocRepository;


import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static otus.deryagina.spring.batch.config.JobConfig.*;

//@Testcontainers
@SpringBatchTest
@Slf4j
@SpringBootTest
class ImportBookJobTest {

//    @ClassRule
//    //@Container
//    public static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.6.2")
//            .withExposedPorts(9200).waitingFor(Wait.forHttp("/"));

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeClass
    public static  void before(){
//        Properties properties = new Properties();
//        String address = elasticsearchContainer.getContainerIpAddress() + elasticsearchContainer.getMappedPort(9200);
//        log.info(address);
//        properties.setProperty("spring.elasticsearch.rest.uris", address);
//        System.setProperties(properties);
    }
    @BeforeAll
    static void setUp() {
      //  elasticsearchContainer.start();
    }
    @BeforeEach
    void testIsContainerRunning() {
     //   assertTrue(elasticsearchContainer.isRunning());
       // recreateIndex();
    }

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
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

    @AfterAll
    static void destroy() {
       // elasticsearchContainer.stop();
    }
}