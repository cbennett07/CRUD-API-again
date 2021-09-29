package com.example.demo.Controller;

import com.example.demo.Model.Todo;
import com.example.demo.Repository.TodoRepository;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
public class TodoController {

    private final TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    //CREATE
    @PostMapping("/todo")
    public Todo createTodo (@RequestBody Todo myTodo){
        return this.repository.save(myTodo);
    }

    //READ
    @GetMapping("/todo/{id}")
    public Todo getTodoById (@PathVariable Long id){
        Todo myTodo = this.repository.findById(id).get();
        return myTodo;
    }

    //UPDATE
    @PatchMapping("/todo/{id}")
    public Todo updateTodoById (@RequestBody Map<String, Object> todoMap, @PathVariable Long id){
        Todo oldTodo = this.repository.findById(id).get();
        todoMap.forEach((key, value) -> {
            switch (key){
                case "description":oldTodo.setDescription((String) value);break;
                case "priority":oldTodo.setPriority((String) value);break;
                case "dueDate":
                    Date myDate = null;
                    try {
                        myDate = new SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    oldTodo.setDueDate(myDate);break;
            }
        });
        return this.repository.save(oldTodo);
    }

    //DELETE
    @DeleteMapping("/todo/{id}")
    public void deleteTodoById(@PathVariable Long id){
        this.repository.deleteById(id);
    }

    //LIST -- will filter on priority var
    @GetMapping("/todo")
    public Iterable<Todo> getAllTodos (@RequestParam(required=false) String priority){
        if (priority!=null){
            return this.repository.findAllByPriorityContains(priority);
        }else{
            return this.repository.findAll();
        }
    }

}
