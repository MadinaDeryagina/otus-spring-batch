package otus.deryagina.spring.batch.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import otus.deryagina.spring.batch.mapper.ModelMapper;
import otus.deryagina.spring.batch.nosql.domain.BookDoc;
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
    public ItemProcessor<Book,BookDoc> bookDocProcessor(ModelMapper modelMapper){
        return modelMapper::entityToDoc;
    }

    @Bean
    public Job importUserJob(Step fromSqlToNoSql) {
        return jobBuilderFactory.get(IMPORT_BOOK_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(fromSqlToNoSql)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NotNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NotNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step fromSqlToNoSql(ItemWriter<BookDoc> bookDocWriter, JpaPagingItemReader<Book> bookReader, ItemProcessor<Book,BookDoc> bookDocProcessor) {
        return stepBuilderFactory.get("fromSqlToNoSql")
                .<Book,BookDoc>chunk(CHUNK_SIZE)
                .reader(bookReader)
                .processor(bookDocProcessor)
                .writer(bookDocWriter)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NotNull Book o) {
                        logger.info(o.toString());
                        logger.info("Конец чтения");
                    }

                    public void onReadError(@NotNull Exception e) {
                        logger.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener<BookDoc>() {
                    public void beforeWrite(@NotNull List list) {
                        logger.info("Начало записи");
                    }

                    public void afterWrite(@NotNull List list) {
                        logger.info("Конец записи");
                    }

                    public void onWriteError(@NotNull Exception e, @NotNull List list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener<Book,BookDoc>() {
                    public void beforeProcess(@NotNull Book o) {
                        logger.info("Начало обработки");
                    }

                    public void afterProcess(@NotNull Book o, BookDoc o2) {
                        logger.info("Конец обработки");
                    }

                    public void onProcessError(@NotNull Book o, @NotNull Exception e) {
                        logger.info("Ошбка обработки");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NotNull ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                    }

                    public void afterChunk(@NotNull ChunkContext chunkContext) {
                        logger.info("Конец пачки");
                    }

                    public void afterChunkError(@NotNull ChunkContext chunkContext) {
                        logger.info("Ошибка пачки");
                    }
                })
                .build();
    }
}
