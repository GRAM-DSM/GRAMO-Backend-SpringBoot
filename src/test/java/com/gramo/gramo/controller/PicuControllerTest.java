package com.gramo.gramo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gramo.gramo.GramoApplication;
import com.gramo.gramo.entity.picu.Picu;
import com.gramo.gramo.entity.picu.PicuRepository;
import com.gramo.gramo.payload.request.PicuRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GramoApplication.class)
class PicuControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private PicuRepository picuRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void cleanup() {
        picuRepository.deleteAll();
    }

    @Test
    public void createPicuTest() throws Exception{

        LocalDate date = LocalDate.now();


        PicuRequest request = PicuRequest.builder()
                .userEmail("userEmail@dsm.hs.kr")
                .description("description")
                .date(date)
                .build();

        mvc.perform(post("/calendar/picu")
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        Picu picu = picuRepository.findAllBy().get(0);

        assertThat(picu.getDescription()).isEqualTo("description");
        assertThat(picu.getUserEmail()).isEqualTo("userEmail@dsm.hs.kr");
        assertThat(picu.getDate()).isEqualTo(date);

    }

    @Test
    public void getPicu() throws Exception {

        Long picu = createPicu();
        Long picu2 = createPicu();
        Long picu3 = createPicu();
        createPicu();

        this.mvc.perform(get("/calendar/picu/"+LocalDate.now())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deletePicuTest() throws Exception {

        Long picuId = createPicu();

        this.mvc.perform(delete("/calendar/picu/"+picuId))
                .andExpect(status().isOk());

        assertThat(picuRepository.findById(picuId).isEmpty()).isEqualTo(true);

    }

    public Long createPicu() {
        return picuRepository.save(
                Picu.builder()
                        .userEmail("userEmail@dsm.hs.kr")
                        .description("desc")
                        .date(LocalDate.now())
                        .build()
        ).getId();
    }
}