package com.gramo.gramo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gramo.gramo.GramoApplication;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.entity.user.enums.Major;
import com.gramo.gramo.payload.response.UserInfoListResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GramoApplication.class)
@ContextConfiguration(classes = GramoApplication.class)
@ActiveProfiles({"test"})
public class UserControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        userRepository.save(createUser("test1"));
        userRepository.save(createUser("test2"));
        userRepository.save(createUser("test3"));
        userRepository.save(createUser("test4"));
    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test1", password = "aaa")
    public void getUserList() throws Exception {
        MvcResult result = this.mvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andReturn();

        UserInfoListResponse userInfoListResponse = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), UserInfoListResponse.class);

        Assertions.assertEquals(userInfoListResponse.getUserInfoResponses().size(), 3);

    }

    private User createUser(String email) {
        return User.builder()
                .email(email)
                .emailStatus(true)
                .major(Major.BACKEND)
                .name("hong!")
                .password("aaa")
                .build();
    }
}
