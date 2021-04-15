package com.gramo.gramo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gramo.gramo.GramoApplication;
import com.gramo.gramo.entity.homework.Homework;
import com.gramo.gramo.entity.homework.HomeworkRepository;
import com.gramo.gramo.entity.homework.embedded.Status;
import com.gramo.gramo.entity.homework.embedded.Term;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.entity.user.enums.Major;
import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.response.HomeworkResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GramoApplication.class)
@ContextConfiguration(classes = GramoApplication.class)
@ActiveProfiles({"test"})
class HomeworkControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private HomeworkRepository homeworkRepository;

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
                        .email("student@dsm.hs.kr")
                        .name("hong!")
                        .password("1234")
                        .major(Major.BACKEND)
                        .build()
        );

        userRepository.save(
                User.builder()
                        .emailStatus(true)
                        .email("teacher@dsm.hs.kr")
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
        homeworkRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "teacher@dsm.hs.kr", password = "1234")
    public void createHomeworkTest() throws Exception{

        LocalDate date = LocalDate.now();

        HomeworkRequest request = HomeworkRequest.builder()
                .description("description")
                .endDate(LocalDate.now().plusDays(1))
                .title("title")
                .major(Major.BACKEND)
                .studentEmail("teacher@dsm.hs.kr")
                .build();

        mvc.perform(post("/homework")
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        Homework homework = homeworkRepository.findAll().get(0);

        assertThat(homework.getDescription()).isEqualTo("description");
        assertThat(homework.getTeacherEmail()).isEqualTo("teacher@dsm.hs.kr");
        assertThat(homework.getMajor()).isEqualTo(Major.BACKEND);

    }

    @Test
    @WithMockUser(value = "teacher@dsm.hs.kr", password = "1234")
    public void getHomeworkTest() throws Exception {

        Long id = createHomework("title1",false,false);
        createHomework("title2",false,false);
        createHomework("title3",false,false);
        createHomework("title4",false,false);

        MvcResult mvcResult = this.mvc.perform(get("/homework/"+id)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        HomeworkResponse response = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(mvcResult.getResponse().getContentAsString(), HomeworkResponse.class);

        Assertions.assertEquals(response.getTitle(), "title1");
    }
//    no token
//    @Test
//    public void getHomeworkTest_unknown() throws Exception {
//
//        Long id = createHomework("title1",false,false);
//        createHomework("title2",false,false);
//        createHomework("title3",false,false);
//        createHomework("title4",false,false);
//
//        this.mvc.perform(get("/homework/"+id)).andDo(print())
//                .andExpect(status().isUnauthorized());
//    }

    @Test
    @WithMockUser(value = "student@dsm.hs.kr", password = "1234")
    public void getAssignedHomeworkTest() throws Exception {

        Long id = createHomework("title1",false,false);
        createHomework("title2",false,true);        // 안보임
        createHomework("title3",true,false);        // 보임(rejected 여부)
        createHomework("title4",false,false);

        this.mvc.perform(get("/homework/assign")).andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(value = "student@dsm.hs.kr", password = "1234")
    public void getSubmittedHomeworkTest() throws Exception {

        Long id = createHomework("title1",false,false);
        createHomework("title2",false,true);        // 얘만 보임
        createHomework("title3",true,false);
        createHomework("title4",false,false);

        this.mvc.perform(get("/homework/submit")).andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(value = "teacher@dsm.hs.kr", password = "1234")
    public void getOrderedHomeworkTest() throws Exception {

        Long id = createHomework("title1",false,false);
        createHomework("title2",false,true);        // 안보임
        createHomework("title3",true,false);        // 보임(rejected 여부)
        createHomework("title4",false,false);

        this.mvc.perform(get("/homework/order")).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "student@dsm.hs.kr", password = "1234")
    public void deleteHomeworkTest_student() throws Exception {

        Long id = createHomework("title",false,false);

        this.mvc.perform(delete("/homework/"+id)).andDo(print())
                .andExpect(status().isForbidden());

        assertThat(homeworkRepository.findById(id).isEmpty()).isEqualTo(false);

    }


    @Test
    @WithMockUser(value = "teacher@dsm.hs.kr", password = "1234")
    public void deleteHomeworkTest_teacher() throws Exception {

        Long id = createHomework("title",false,false);

        this.mvc.perform(delete("/homework/"+id)).andDo(print())
                .andExpect(status().isOk());

        assertThat(homeworkRepository.findById(id).isEmpty()).isEqualTo(true);

    }

    @Test
    @WithMockUser(value = "student@dsm.hs.kr", password = "1234")
    public void submitHomeworkTest() throws Exception {

        Long id = createHomework("title",false,false);
        createHomework("title",false,false);

        this.mvc.perform(patch("/homework/"+id)).andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(homeworkRepository.findById(id).get().getStatus().getIsSubmitted(),true);
    }

    @Test
    @WithMockUser(value = "teacher@dsm.hs.kr", password = "1234")
    public void submitHomeworkTest_not_student() throws Exception {

        Long id = createHomework("title",false,false);
        createHomework("title",false,false);
        createHomework("title",false,false);

        this.mvc.perform(patch("/homework/"+id)).andDo(print())
                .andExpect(status().isForbidden());

        Assertions.assertEquals(homeworkRepository.findById(id).get().getStatus().getIsSubmitted(),false);

    }


    @Test
    @WithMockUser(value = "teacher@dsm.hs.kr", password = "1234")
    public void rejectHomeworkTest() throws Exception {

        Long id = createHomework("title",false,false);
        createHomework("title2",false,false);

        this.mvc.perform(patch("/homework/reject/"+id)).andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(homeworkRepository.findById(id).get().getStatus().getIsRejected(),true);
    }

    @Test
    @WithMockUser(value = "student@dsm.hs.kr", password = "1234")
    public void rejectHomeworkTest_not_teacher() throws Exception {

        Long id = createHomework("title",false,false);
        createHomework("title",false,false);

        this.mvc.perform(patch("/homework/reject/"+id)).andDo(print())
                .andExpect(status().isForbidden());

        Assertions.assertEquals(homeworkRepository.findById(id).get().getStatus().getIsRejected(),false);

    }

    public Long createHomework(String title, boolean isRejected, boolean isSubmitted) {
        Status status = Status.builder()
                .isSubmitted(isSubmitted)
                .isRejected(isRejected)
                .build();

        return homeworkRepository.save(
                Homework.builder()
                        .teacherEmail("teacher@dsm.hs.kr")
                        .description("description")
                        .term(Term.builder()
                                .endDate(LocalDate.now().plusDays(1))
                                .startDate(LocalDate.now()).build())
                        .major(Major.BACKEND)
                        .studentEmail("student@dsm.hs.kr")
                        .title(title)
                        .status(status)
                        .build()
        ).getId();
    }
}