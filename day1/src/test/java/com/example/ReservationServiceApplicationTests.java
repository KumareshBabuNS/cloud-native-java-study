package com.example;

import com.example.domain.Reservation;
import com.example.domain.repository.ReservationRepository;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.List;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ReservationServiceApplicationTests {

    private Logger logger = LoggerFactory.getLogger(ReservationServiceApplicationTests.class);

    @Autowired
    private  WebApplicationContext context;

    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaTypes.HAL_JSON.getType(),
            MediaTypes.HAL_JSON.getSubtype(),
            Charset.forName("utf8"));

    private String reservations = "reservations";

    @Autowired
    ReservationRepository repository;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        List<Reservation> reservationList = repository.findAll();
        assertThat(reservationList.size(), is(5));
    }

    @Test
    public void root_에_접근하면_맵핑된_링크와_프로필을_돌려준다_200() throws Exception {
        //given

        //when then
        String body = this.mockMvc.perform(get("/" ))
                .andExpect(content().contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links", "reservations").exists())
                .andExpect(jsonPath("$._links", "babies").exists())
                .andReturn().getResponse().getContentAsString();

//        assertThat(body, is(
//                "{\n" +
//                        "  \"_links\" : {\n" +
//                        "    \"reservations\" : {\n" +
//                        "      \"href\" : \"http://localhost/reservations{?page,size,sort}\",\n" +
//                        "      \"templated\" : true\n" +
//                        "    },\n" +
//                        "    \"babies\" : {\n" +
//                        "      \"href\" : \"http://localhost/babies{?page,size,sort}\",\n" +
//                        "      \"templated\" : true\n" +
//                        "    },\n" +
//                        "    \"profile\" : {\n" +
//                        "      \"href\" : \"http://localhost/profile\"\n" +
//                        "    }\n" +
//                        "  }\n" +
//                        "}"
//        ));

          // logger.info(body);
    }

    @Test
    public void 모든_예약을_조회한다_200() throws Exception {
        //given

        //when then
        String body = this.mockMvc.perform(get("/" + reservations))
                .andExpect(content().contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations[*]", hasSize(5))) // A,B,C,D,E
                .andExpect(jsonPath("$._embedded.reservations[0].reservationName", is("A")))
                .andExpect(jsonPath("$._embedded.reservations[1].reservationName", is("B")))
                .andExpect(jsonPath("$._embedded.reservations[2].reservationName", is("C")))
                .andExpect(jsonPath("$._embedded.reservations[3].reservationName", is("D")))
                .andExpect(jsonPath("$._embedded.reservations[4].reservationName", is("E")))
                .andReturn().getResponse().getContentAsString();

         // logger.info(body);
    }

    @Test
    public void BDD_템플릿_대신_jayway_를_사용() {
        when().
                get("/" + reservations).
        then().
                statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void 도메인명의_복수형이_ies_로_끝나도_리소스_URI를_자동_생성한다() throws Exception {
        //given
        String babies = "babies";

        //when then
        String body = this.mockMvc.perform(get("/" + babies))
                .andExpect(content().contentType(contentType))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

         logger.info(body);
    }

    @Test
    public void 예약명으로_예약을_조회한다_200() throws Exception {
        //given
        String path1 = "search";
        String path2 = "by-name";
        String param = "rn";
        String reservationName = "A";

        //when then
        String body = this.mockMvc.perform(get("/" + reservations + "/" + path1 + "/" + path2 + "?" + param + "=" + reservationName))
                .andExpect(content().contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations[0].reservationName", is(reservationName)))
                .andReturn().getResponse().getContentAsString();

        assertThat(body, is(
            "{\n" +
            "  \"_embedded\" : {\n" +
            "    \"reservations\" : [ {\n" +
            "      \"reservationName\" : \"A\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"http://localhost/reservations/1\"\n" +
            "        },\n" +
            "        \"reservation\" : {\n" +
            "          \"href\" : \"http://localhost/reservations/1\"\n" +
            "        },\n" +
            "        \"Search Google\" : {\n" +
            "          \"href\" : \"https://www.google.co.kr/?gfe_rd=cr&ei=v0lnWPGzFPTZ8AeV06Fo#newwindow=1&q=1\"\n" +
            "        }\n" +
            "      }\n" +
            "    } ]\n" +
            "  },\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"http://localhost/reservations/search/by-name?rn=A\"\n" +
            "    }\n" +
            "  }\n" +
            "}"
        ));
         // logger.info(body);
    }

    @Test
    public void 존재하지_않는_예약명으로_예약을_조회한다_200() throws Exception {
        //given
        String path1 = "search";
        String path2 = "by-name";
        String param = "rn";
        String reservationName = "Z";

        //when then
        String body = this.mockMvc.perform(get("/" + reservations + "/" + path1 + "/" + path2 + "?" + param + "=" + reservationName))
                .andExpect(content().contentType(contentType))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(body, is(
            "{\n" +
            "  \"_embedded\" : {\n" +
            "    \"reservations\" : [ ]\n" +
            "  },\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"http://localhost/reservations/search/by-name?rn=Z\"\n" +
            "    }\n" +
            "  }\n" +
            "}"
        ));

         // logger.info(body);
    }

    @Test
    public void path_를_누락하면_사이트_내_하이퍼링크_형식을_돌려준다_200() throws Exception {
        //given
        String search = "search"; // /by-name?rn= 형식의 path와 쿼리스트링 누락

        //when then
        String body = this.mockMvc.perform(get("/" + reservations +  "/" + search))
                .andExpect(content().contentType(contentType))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(body, is(
            "{\n" +
            "  \"_links\" : {\n" +
            "    \"findByReservationName\" : {\n" +
            "      \"href\" : \"http://localhost/reservations/search/by-name{?rn}\",\n" +
            "      \"templated\" : true\n" +
            "    },\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"http://localhost/reservations/search\"\n" +
            "    }\n" +
            "  }\n" +
            "}"
        ));

        // logger.info(body);
    }

    @Test
    public void HATEOAS_를_통해_동적으로_리소스를_탐색하고_재_요청한다() throws Exception {
        //TODO
    }

    @Test
    public void 존재_하지_않는_End_Point_요청한다_404() throws Exception {
        //given
        String doNotExist = "do-not-exist";

        //when
        this.mockMvc.perform(get("/" + doNotExist))
                .andExpect(status().isNotFound());

        //then
        //exception
    }
}
