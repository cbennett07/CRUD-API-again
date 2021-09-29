package com.example.demo.Controller;

import ch.qos.logback.classic.pattern.DateConverter;
import com.example.demo.Model.Todo;
import com.example.demo.Repository.TodoRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TodoRepository repository;

    //CREATE
    @Test
    @Transactional
    @Rollback
    void createTodo () throws Exception{
        MockHttpServletRequestBuilder request = post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"description\": \"watch SB LIV\",\n" +
                        "    \"priority\": \"high\",\n" +
                        "    \"dueDate\": \"2020-02-02\"\n" +
                        "}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priority", is("high") ))
                .andExpect(jsonPath("$.description", is("watch SB LIV") ));
    }
    //READ
    @Test
    @Transactional
    @Rollback
    void returnTodoById () throws Exception{
        Todo myTodo = new Todo();
        myTodo.setDescription("my desc");
        myTodo.setPriority("low");
        //create Date and update
        String sDate = "2022-09-18";
        Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        myTodo.setDueDate(myDate);
        //save to repo
        repository.save(myTodo);

        MockHttpServletRequestBuilder request = get("/todo/" + myTodo.getId())
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("my desc")))
                .andExpect(jsonPath("$.priority", is("low")))
                .andExpect(jsonPath("$.dueDate", is("2022-09-18")));
    }
    //UPDATE
    @Test
    @Transactional
    @Rollback
    void updateTodo () throws Exception{
        Todo myTodo = new Todo();
        myTodo.setDescription("my new desc");
        myTodo.setPriority("low");
        //create Date and update
        String sDate = "2016-09-24";
        Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        myTodo.setDueDate(myDate);
        //save to repo
        repository.save(myTodo);

        //create the new date in String
        String newSDate = "2022-02-07";

        MockHttpServletRequestBuilder request = patch("/todo/" + myTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"description\": \"watch SB LVI\",\n" +
                        "    \"priority\": \"high\",\n" +
                        "    \"dueDate\": \"" + newSDate + "\"\n" +
                        "}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("watch SB LVI")))
                .andExpect(jsonPath("$.dueDate", is(newSDate)))
                .andExpect(jsonPath("$.priority", is("high")));
    }
    //DELETE
    @Test
    @Transactional
    @Rollback
    void deleteTodo () throws Exception{
        Todo myTodo = new Todo();
        myTodo.setDescription("Dylan is Born");
        myTodo.setPriority("high");
        //create Date and update
        String sDate = "2016-09-24";
        Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        myTodo.setDueDate(myDate);
        //save to repo
        repository.save(myTodo);

        MockHttpServletRequestBuilder request = delete("/todo/" + myTodo.getId())
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
    //LIST
    @Test
    @Transactional
    @Rollback
    void listTodosFilterPriority () throws Exception{
        //add a Todo
        Todo myTodo = new Todo();
        myTodo.setDescription("Dylan is Born");
        myTodo.setPriority("high");
        //create Date and update
        String sDate = "2016-09-24";
        Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        myTodo.setDueDate(myDate);
        //save to repo
        repository.save(myTodo);
        //get id back
        Integer firstId = myTodo.getId().intValue();
        //add another Todo
        Todo myTodo2 = new Todo();
        myTodo2.setDescription("Carter is Born");
        myTodo2.setPriority("low");
        //create Date and update
        sDate = "2012-03-14";
        myDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        myTodo2.setDueDate(myDate);
        //save to repo
        repository.save(myTodo2);
        //get id back
        Integer secondId = myTodo2.getId().intValue();

        MockHttpServletRequestBuilder request = get("/todo?priority=low")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(secondId)));


        request = get("/todo?priority=high")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(firstId)));

    }


}
