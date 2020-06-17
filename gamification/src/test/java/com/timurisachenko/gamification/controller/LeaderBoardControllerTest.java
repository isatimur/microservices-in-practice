package com.timurisachenko.gamification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timurisachenko.gamification.domain.LeaderBoardRow;
import com.timurisachenko.gamification.service.LeaderBoardService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(LeaderBoardController.class)
public class LeaderBoardControllerTest {
    @MockBean
    private LeaderBoardService service;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<List<LeaderBoardRow>> json;
    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }
    @SneakyThrows
    @Test
    public void getLeaderBoardTest() {
        LeaderBoardRow leaderBoardRow1 = new LeaderBoardRow(1l,100l);
        LeaderBoardRow leaderBoardRow2 = new LeaderBoardRow(2l,500l);
        List<LeaderBoardRow> leaderBoard = new ArrayList<>();
        Collections.addAll(leaderBoard,leaderBoardRow1,leaderBoardRow2);
        given(service.getCurrentLeaderBoard()).willReturn(leaderBoard);
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get("/leaders")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(json.write(leaderBoard).getJson());

    }

}
