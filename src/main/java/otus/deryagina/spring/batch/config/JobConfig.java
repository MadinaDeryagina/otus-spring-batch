package otus.deryagina.spring.batch.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import otus.deryagina.spring.batch.mapper.ModelMapper;
import otus.deryagina.spring.batch.nosql.domain.BookDoc;
import otus.deryagina.spring.batch.processor.BookDocProcessor;
import otus.deryagina.spring.batch.sql.domain.Book;
import otus.deryagina.spring.batch.writer.ElasticsearchItemWriter;

import javax.persistence.EntityManagerFactory;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class JobConfig {
    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger("Batch");

    public static final String IMPORT_BOOK_JOB_NAME = "importBookJob";


    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;


    @StepScope
    @Bean
    public JpaPagingItemReader<Book> bookReader(EntityManagerFactory entityManagerFactory){
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookItemReader")
                .queryString("select b from Book b")
                .entityManagerFactory(entityManagerFactory)
                .saveState(true)
                .pageSize(5)
                .build();

    }

    @StepScope
    @Bean
    public ElasticsearchItemWriter<BookDoc> bookDocWriter(ElasticsearchRepository<BookDoc,String> bookDocRepository){
        return new ElasticsearchItemWriter<>(bookDocRepository);
    }

    @StepScope
    @Bean
    public BookDocProcessor bookDocProcessor(ModelMapper modelMapper){
        return new BookDocProcessor(modelMapper);
    }

    @Bean
    public Job importUserJob(Step step1) {
        return jobBuilderFactory.get(IMPORT_BOOK_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step fromSqlToNoSql(ElasticsearchItemWriter<BookDoc> bookDocWriter, JpaPagingItemReader<Book> bookReader, ItemProcessor bookDocProcessor) {
        return stepBuilderFactory.get("fromSqlToNoSql")
                .chunk(CHUNK_SIZE)
                .reader(bookReader)
                .processor(bookDocProcessor)
                .writer(bookDocWriter)
                .listener(new ItemReadListener() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(Object o) {
                        logger.info(o.toString());
                        logger.info("Конец чтения");
                    }

                    public void onReadError(Exception e) {
                        logger.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener() {
                    public void beforeWrite(List list) {
                        logger.info("Начало записи");
                    }

                    public void afterWrite(List list) {
                        logger.info("Конец записи");
                    }

                    public void onWriteError(Exception e, List list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener() {
                    public void beforeProcess(Object o) {
                        logger.info("Начало обработки");
                    }

                    public void afterProcess(Object o, Object o2) {
                        logger.info("Конец обработки");
                    }

                    public void onProcessError(Object o, Exception e) {
                        logger.info("Ошбка обработки");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                    }

                    public void afterChunk(ChunkContext chunkContext) {
                        logger.info("Конец пачки");
                    }

                    public void afterChunkError(ChunkContext chunkContext) {
                        logger.info("Ошибка пачки");
                    }
                })
                .build();
    }
}
