package com.mdev.junite.service;

import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.PERIOD;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)

public class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");

    private UserService userService;

    @BeforeAll
    static void init() {
        System.out.println("Before All: ");
    }

    @BeforeEach
    void prepare() {
        System.out.println("Before Each: " + this);
        userService = new UserService();
    }


    @Test
    void usersEmptyIfNoUserAdded() {
        System.out.println("Test_1: " + this);
        var users = userService.getAll();
        assertTrue(users.isEmpty());
    }

    @Test
    void loginSuccessIfUserExists() {
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUserName(), IVAN.getPassword());

        assertThat(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertThat(user).isEqualTo(IVAN));

       //assertTrue(maybeUser.isPresent());
       // maybeUser.ifPresent(user -> assertEquals(IVAN, user));

    }
    @Test
    void usersConvertedToMapById(){
        userService.add(IVAN, PETR);

        Map<Integer, User> users = userService.getAllConvertedById();
        assertAll(
                ()-> assertThat(users).containsKeys(IVAN.getId(), PETR.getId()),
                ()-> assertThat(users).containsValues(IVAN, PETR)
        );
    }


    @Test
    void loginFaildIfPasswordIsNotCorrect(){
        userService.add(IVAN);
        var maybeUser = userService.login(IVAN.getUserName(), "dummy");
        assertTrue(maybeUser.isEmpty());
    }
    @Test
    void loginFaildIfUserDoesNotExist(){
        userService.add(IVAN);
        var maybeUser = userService.login("dummy", IVAN.getPassword());

        assertTrue(maybeUser.isEmpty());
    }

    @Test
    void userSizeIfUserAdded() {
        System.out.println("Test_2: " + this);
        userService.add(IVAN);
        userService.add(PETR);

        var users = userService.getAll();

        assertThat(users).hasSize(2);
        //assertEquals(2, users.size());
    }

    @AfterEach
    void deleteDataFromDataBase() {
        System.out.println("After Each: " + this);
    }

    @AfterAll
    static void closeConnectionPool() {
        System.out.println("After All: ");
    }
}
