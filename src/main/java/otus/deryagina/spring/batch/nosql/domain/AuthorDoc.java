package otus.deryagina.spring.batch.nosql.domain;

import lombok.Data;

@Data
public class AuthorDoc {

    private long idFromSql;

    private String fullName;
}
