package br.com.tmdev.pocrediscache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SomeController {

    @Autowired
    private SomeService someService;

    @GetMapping("/redis/books")
    public List<Book> getBooksRedis() {
        return someService.getBooksRedis();
    }

    @DeleteMapping("/redis/books")
    public void deleteBooksRedis() {
        someService.deleteBooksRedis();
    }

    @GetMapping("/memory/books")
    public List<Book> getBooksMemory() {
        return someService.getBooksMemory();
    }

    @DeleteMapping("/memory/books")
    public void deleteBooksMemory() {
        someService.deleteBooksMemory();
    }

}
