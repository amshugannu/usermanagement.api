package com.example.usermanagement.api;

import com.example.usermanagement.api.model.User;
import com.example.usermanagement.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Arrays;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();

        objectMapper.registerModule(new JavaTimeModule());

        User initialUser1 = new User("Jhon Doe", "john.doe@example.com");
        User initialUser2 = new User ("Jane Smith","janesmith@example.com");

        List<User> savedInitialUsers = userRepository.saveAll(Arrays.asList(initialUser1,initialUser2));

        this.user1 = savedInitialUsers.get(0);
        this.user2 = savedInitialUsers.get(1);

        long initialUserCount;

        List<User> manyUsers = IntStream.rangeClosed(1, 25)
                .mapToObj(i -> new User("User " + i, "user" + i + "@example.com"))
                .collect(Collectors.toList());

        userRepository.saveAll(manyUsers);

        initialUserCount = userRepository.count();
//        System.out.println("User1 ID: " + user1.getId());
//        System.out.println("Total users in DB: " + userRepository.count());


    }

    @Test
    public void testGetUserByIdFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", user1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(user1.getId().intValue())))
                .andExpect( jsonPath("$.name",is(user1.getName())))
                .andExpect( jsonPath("$.email",is(user1.getEmail())));
    }
    @Test
    void testCreateUser() throws Exception{
        //Arrange
        User newUser1  = new User("Alice Brown","alice.brown@example.com");
        User newUser2 = new User("Cameron Green","green@example.com");
//        List<User> newUsers = Arrays.asList(newUser1,newUser2);

        //ACT
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",notNullValue()))
                .andExpect(jsonPath("$.name",is("Alice Brown")))
                .andExpect(jsonPath("$.email",is("alice.brown@example.com")));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",notNullValue()))
                .andExpect(jsonPath("$.name",is("Cameron Green")))
                .andExpect(jsonPath("$.email",is("green@example.com")));
    }

    void testDeleteUserFound{

    }
    void testDeleteUserNotFound{

    }
}
