package br.com.tmdev.pocrediscache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class BookRepository {

    private static final Logger LOG = LoggerFactory.getLogger(BookRepository.class);

    public List<Book> getBooks() {
        LOG.info("Returning books from repository");
        return Arrays.asList(
                new Book(1, "Java 11", "12345", 11.9),
                new Book(2, "Redis", "54321", 13.5)
        );
    }
}
