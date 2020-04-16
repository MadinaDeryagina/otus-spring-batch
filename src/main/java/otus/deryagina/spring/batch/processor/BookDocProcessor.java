package otus.deryagina.spring.batch.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;
import otus.deryagina.spring.batch.mapper.ModelMapper;
import otus.deryagina.spring.batch.nosql.domain.BookDoc;
import otus.deryagina.spring.batch.sql.domain.Book;

@RequiredArgsConstructor
public class BookDocProcessor implements ItemProcessor<Book, BookDoc> {

    private final ModelMapper modelMapper;

    @Override
    public BookDoc process(Book book)  {
        return modelMapper.entityToDoc(book);

    }

}
