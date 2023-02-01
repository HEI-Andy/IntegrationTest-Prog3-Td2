package integration;

import app.foot.FootApi;
<<<<<<< HEAD
import app.foot.controller.rest.Match;
import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.exception.BadRequestException;
import app.foot.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
=======
import app.foot.controller.rest.*;
import com.fasterxml.jackson.databind.ObjectMapper;
>>>>>>> origin/dev
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
<<<<<<< HEAD
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtils.assertThrowsExceptionMessage;
import static utils.TestUtils.convertFromHttpResponse;
import static utils.TestUtils.match2;
import static utils.TestUtils.normalizeKnownExceptionMessage;
=======
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
>>>>>>> origin/dev

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
class MatchIntegrationTest {
<<<<<<< HEAD
    private static final int MATCH_ID_3 = 3;
    private static final int NON_EXISTING_MATCH_ID = 0;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();  //Allow 'java.time.Instant' mapping
    @Autowired
    private MockMvc mockMvc;


    private PlayerScorer scorer_1() {
        return PlayerScorer.builder()
                .player(
                        Player.builder()
                                .id(1)
                                .name("J1")
                                .teamName("E1")
                                .isGuardian(false)
                                .build()
                )
                .isOG(false)
                .scoreTime(30)
                .build();
    }
=======
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();  //Allow 'java.time.Instant' mapping
>>>>>>> origin/dev

    @Test
    void read_match_by_id_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/matches/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Match actual = objectMapper.readValue(
                response.getContentAsString(), Match.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
<<<<<<< HEAD
        assertEquals(match2(), actual);
    }


    @Test
    void read_match_by_id_ko() throws Exception {
        RequestBuilder request = get("/matches/{matchId}", NON_EXISTING_MATCH_ID);

        ServletException exception = assertThrows(ServletException.class, () -> mockMvc.perform(request));

        String exceptionMessage = normalizeKnownExceptionMessage(
                "Match#" + NON_EXISTING_MATCH_ID + " not found.",
                HttpStatus.NOT_FOUND
        );

        assertThrowsExceptionMessage(exceptionMessage, NotFoundException.class, () -> {
            throw exception.getRootCause();
        });
    }

    @Test
    void read_matches_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<Match> actual = convertFromHttpResponse(objectMapper, response, Match.class);

        assertEquals(3, actual.size());
        assertTrue(actual.contains(match2()));
    }

    @Test
    void put_goals_ok() throws Exception {
        RequestBuilder request = post("/matches/{matchId}/goals", MATCH_ID_3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(scorer_1())));

        MockHttpServletResponse response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Match actual = objectMapper.readValue(
                response.getContentAsString(), Match.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(actual.getTeamA().getScorers().contains(scorer_1()));
    }


    @Test
    void put_goals_ko() throws Exception {
        PlayerScorer nullTimeScorer = scorer_1().toBuilder()
                .scoreTime(null)
                .build();

        RequestBuilder request = post("/matches/{matchId}/goals", MATCH_ID_3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(nullTimeScorer)));

        ServletException exception = assertThrows(ServletException.class, ()-> mockMvc.perform(request));

        String exceptionMessage = normalizeKnownExceptionMessage(
                "Bad request",
                HttpStatus.BAD_REQUEST
        );

        assertThrowsExceptionMessage(exceptionMessage, BadRequestException.class,()->{
            throw exception.getRootCause();
        });
=======
        assertEquals(expectedMatch2(), actual);
    }

    private static Match expectedMatch2() {
        return Match.builder()
                .id(2)
                .teamA(teamMatchA())
                .teamB(teamMatchB())
                .stadium("S2")
                .datetime(Instant.parse("2023-01-01T14:00:00Z"))
                .build();
    }

    private static TeamMatch teamMatchB() {
        return TeamMatch.builder()
                .team(team3())
                .score(0)
                .scorers(List.of())
                .build();
    }

    private static TeamMatch teamMatchA() {
        return TeamMatch.builder()
                .team(team2())
                .score(2)
                .scorers(List.of(PlayerScorer.builder()
                                .player(player3())
                                .scoreTime(70)
                                .isOG(false)
                                .build(),
                        PlayerScorer.builder()
                                .player(player6())
                                .scoreTime(80)
                                .isOG(true)
                                .build()))
                .build();
    }

    private static Team team3() {
        return Team.builder()
                .id(3)
                .name("E3")
                .build();
    }

    private static Player player6() {
        return Player.builder()
                .id(6)
                .name("J6")
                .isGuardian(false)
                .build();
    }

    private static Player player3() {
        return Player.builder()
                .id(3)
                .name("J3")
                .isGuardian(false)
                .build();
    }

    private static Team team2() {
        return Team.builder()
                .id(2)
                .name("E2")
                .build();
>>>>>>> origin/dev
    }
}
