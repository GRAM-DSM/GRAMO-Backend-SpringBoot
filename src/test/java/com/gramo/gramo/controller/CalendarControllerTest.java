package com.gramo.gramo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gramo.gramo.GramoApplication;
import com.gramo.gramo.entity.picu.Picu;
import com.gramo.gramo.entity.picu.PicuRepository;
import com.gramo.gramo.entity.plan.Plan;
import com.gramo.gramo.entity.plan.PlanRepository;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.entity.user.enums.Major;
import com.gramo.gramo.payload.response.CalendarContentResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = GramoApplication.class)
@ActiveProfiles({"test"})
public class CalendarControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PicuRepository picuRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        User user = createUser();

        createPicu(user);
        createPicu(user);
        createPicu(user);

        createPlan(user);
        createPlan(user);
        createPlan(user);
        createPlan(user);
    }

    @AfterEach
    public void cleanUp() {
        planRepository.deleteAll();
        picuRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "email", password = "pwd")
    public void calendar_list_test() throws Exception {
        MvcResult result = mvc.perform(get("/calendar")
                .param("date", "2021-04-19"))
                .andExpect(status().isOk())
                .andReturn();

        List<CalendarContentResponse> responses = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(result.getResponse().getContentAsString(), new TypeReference<List<CalendarContentResponse>>() {});

        Assertions.assertEquals(responses.get(0).getDate(), LocalDate.of(2021, 04, 01));
        Assertions.assertEquals(responses.get(0).getPicuCount(), 3);
        Assertions.assertEquals(responses.get(0).getPlanCount(), 4);


    }

    private User createUser() {
        return userRepository.save(
                User.builder()
                        .password(passwordEncoder.encode("pwd"))
                        .name("name")
                        .major(Major.BACKEND)
                        .emailStatus(true)
                        .email("email")
                        .build()
        );

    }

    private Picu createPicu(User user) {
        return picuRepository.save(
                Picu.builder()
                        .date(LocalDate.of(2021,04,1))
                        .description("desc")
                        .userEmail(user.getEmail())
                        .build()
        );
    }

    private Plan createPlan(User user) {
        return planRepository.save(
                Plan.builder()
                        .date(LocalDate.of(2021,04,1))
                        .description("desc")
                        .title("title")
                        .build()
        );
    }
}
