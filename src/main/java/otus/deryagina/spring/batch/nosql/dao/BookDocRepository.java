package otus.deryagina.spring.batch.nosql.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import otus.deryagina.spring.batch.nosql.domain.BookDoc;

@Repository
public interface BookDocRepository extends ElasticsearchRepository<BookDoc,String> {
}
