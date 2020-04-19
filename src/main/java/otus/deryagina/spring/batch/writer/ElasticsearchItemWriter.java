package otus.deryagina.spring.batch.writer;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

@RequiredArgsConstructor
public class ElasticsearchItemWriter<T> implements ItemWriter<T> {

    private final ElasticsearchRepository<T, String> repository;

    @Override
    public void write(@NotNull List<? extends T> list) {
        repository.saveAll(list);
    }
}
