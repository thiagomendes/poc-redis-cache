package br.com.tmdev.pocrediscache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SomeService {

    private static final Logger LOG = LoggerFactory.getLogger(SomeService.class);

    @Autowired
    private BookRepository bookRepository;

    @Cacheable(value = "books", cacheManager = "redisCacheManager")
    public List<Book> getBooksRedis() {
        LOG.info("getBooksRedis");
        return bookRepository.getBooks();
    }

    @Cacheable(value = "books", cacheManager = "inMemoryCacheManager")
    public List<Book> getBooksMemory() {
        LOG.info("getBooksMemory");
        return bookRepository.getBooks();
    }

    @CacheEvict(value = "books", cacheManager = "redisCacheManager")
    public void deleteBooksRedis() {
        LOG.info("deleteBooksRedis");
        return;
    }

    @CacheEvict(value = "books", cacheManager = "inMemoryCacheManager")
    public void deleteBooksMemory() {
        LOG.info("deleteBooksMemory");
        return;
    }
}
