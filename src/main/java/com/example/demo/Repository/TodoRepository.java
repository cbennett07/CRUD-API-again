package com.example.demo.Repository;
import com.example.demo.Model.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long>{

    Iterable<Todo> findAllByPriorityContains(String priority);
}
