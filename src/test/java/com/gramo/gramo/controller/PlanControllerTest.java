package com.gramo.gramo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gramo.gramo.GramoApplication;
import com.gramo.gramo.entity.plan.Plan;
import com.gramo.gramo.entity.plan.PlanRepository;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.entity.user.enums.Major;
import com.gramo.gramo.payload.request.PlanRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GramoApplication.class)
@ContextConfiguration(classes = GramoApplication.class)
@ActiveProfiles({"test"})
class PlanControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        userRepository.save(
                User.builder()
                        .emailStatus(true)
                        .email("emailedUser@dsm.hs.kr")
                        .name("hong!")
                        .password("1234")
                        .major(Major.BACKEND)
                        .build()
        );

        userRepository.save(
                User.builder()
                        .emailStatus(false)
                        .email("notUser@dsm.hs.kr")
                        .name("nam!")
                        .password("1234")
                        .major(Major.BACKEND)
                        .build()
        );
    }

    @AfterEach
    public void cleanup() {
        planRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "emailedUser@dsm.hs.kr", password = "1234")
    public void createPlanTest() throws Exception{

        LocalDate date = LocalDate.now();


        PlanRequest request = PlanRequest.builder()
                .title("title")
                .description("description")
                .date(date)
                .build();

        mvc.perform(post("/calendar/plan")
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        Plan plan = planRepository.findAllBy().get(0);

        assertThat(plan.getDescription()).isEqualTo("description");
        assertThat(plan.getTitle()).isEqualTo("title");
        assertThat(plan.getDate()).isEqualTo(date);

    }

    @Test
    @WithMockUser(value = "emailedUser@dsm.hs.kr", password = "1234")
    public void getPlan() throws Exception {

        Long planId = createPlan("title");
        createPlan("title3");

        this.mvc.perform(get("/calendar/plan/"+LocalDate.now()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "emailedUser@dsm.hs.kr", password = "1234")
    public void deletePlanTest() throws Exception {

        Long planId = createPlan("title");

        this.mvc.perform(delete("/calendar/plan/"+planId))
                .andExpect(status().isOk());

        assertThat(planRepository.findById(planId).isEmpty()).isEqualTo(true);

    }

    public Long createPlan(String title) {
        return planRepository.save(
                Plan.builder()
                        .title(title)
                        .description("desc")
                        .date(LocalDate.now())
                        .build()
        ).getId();
    }
}