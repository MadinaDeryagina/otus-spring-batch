package otus.deryagina.spring.batch.sql.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import otus.deryagina.spring.batch.sql.domain.Book;


import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = "book-author-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Book> findAll();

    @EntityGraph(value = "book-author-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Book> findById(long bookId);

    @EntityGraph(value = "book-author-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Book> findAllByTitle(String title);
}
