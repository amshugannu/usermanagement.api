package com.example.usermanagement.api;

import com.example.usermanagement.api.model.User;
import com.example.usermanagement.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;
    private User user1;
    private User user2;

    @BeforeEach
    void setup(){
        userRepository.deleteAllInBatch();

        user1 = new User("Alice Wonderland", "alice@example.com");
        user2 = new User("Bob the builder","bob@example.com");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindByEmailFound(){
        Optional<User> foundUser = userRepository.findByEmail(user1.getEmail());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(user1.getName());
        assertThat(foundUser.get().getEmail()).isEqualTo(user1.getEmail());
        assertThat(foundUser.get().getId()).isEqualTo(user1.getId());

    }

    @Test
    void testFindByEmailNotFound(){
        Optional<User> foundUser = userRepository.findByEmail("nonexistingemail@example.com");
        assertThat(foundUser).isNotPresent();
    }
    @Test
    void testSaveUser(){
        User newUser = new User("Charlie Chaplin","charlie@example.com");
        User saveduser = userRepository.save(newUser);
        assertThat(saveduser).isNotNull();
        assertThat(saveduser.getId()).isNotNull();
        assertThat(saveduser.getName()).isEqualTo("Charlie Chaplin");
    }

    @Test
    void testUpdateUserRecord(){
        Optional<User> optionalUser = userRepository.findById(user1.getId());
        assertThat(optionalUser).isPresent();

        User user3 = optionalUser.get();

        String newName = "Kieron Pollard";
        String newEmail = "pollard@example.com";

        user3.setName(newName);
        user3.setEmail(newEmail);

        userRepository.save(user3);

        Optional<User> updatedUserOptional = userRepository.findById(user1.getId());
        assertThat(updatedUserOptional).isPresent();

        User updatedUser = updatedUserOptional.get();
        assertThat(updatedUser.getEmail()).isEqualTo(newEmail);
        assertThat(updatedUser.getName()).isEqualTo(newName);

    }
}
