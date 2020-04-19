package otus.deryagina.spring.batch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import otus.deryagina.spring.batch.nosql.domain.AuthorDoc;
import otus.deryagina.spring.batch.nosql.domain.BookDoc;
import otus.deryagina.spring.batch.nosql.domain.GenreDoc;
import otus.deryagina.spring.batch.sql.domain.Author;
import otus.deryagina.spring.batch.sql.domain.Book;
import otus.deryagina.spring.batch.sql.domain.Genre;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ModelMapper{

    @Mappings({
            @Mapping(source = "id", target = "idFromSql"),
            @Mapping(target = "id", ignore = true)
    })
    BookDoc entityToDoc(Book book);

    List<BookDoc> entityToDoc(List<Book> books);

    List<GenreDoc> genreEntityListToGenreDocList(List<Genre> genres);

    List<AuthorDoc> authorEntityListToAuthorDocList(List<Author> authors);

    @Mappings({
            @Mapping(source = "id", target = "idFromSql"),
    })
    AuthorDoc entityToDoc(Author author);

    @Mappings({
            @Mapping(source = "id", target = "idFromSql"),
    })
    GenreDoc entityToDoc(Genre genre);

}
