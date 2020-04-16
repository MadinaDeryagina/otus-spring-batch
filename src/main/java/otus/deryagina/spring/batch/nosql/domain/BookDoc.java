package otus.deryagina.spring.batch.nosql.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;


@Data
@Document(indexName = "library", type = "book")
public class BookDoc {
    @Id
    private String id;

    private long idFromSql;

    private String title;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<AuthorDoc> authors;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<GenreDoc> genres;
}
