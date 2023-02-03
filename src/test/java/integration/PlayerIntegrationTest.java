package integration;

import app.foot.FootApi;
import app.foot.controller.rest.Player;
import app.foot.exception.BadRequestException;
import app.foot.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static utils.TestUtils.assertThrowsExceptionMessage;
import static utils.TestUtils.convertFromHttpResponse;
import static utils.TestUtils.normalizeKnownExceptionMessage;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
@Slf4j
class PlayerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final int NON_EXISTING_PLAYER_ID = 51;

    Player player1() {
        return Player.builder()
                .id(1)
                .name("J1")
                .teamName("E1")
                .isGuardian(false)
                .build();
    }

    Player player2() {
        return Player.builder()
                .id(2)
                .name("J2")
                .teamName("E1")
                .isGuardian(false)
                .build();
    }

    Player player3() {
        return Player.builder()
                .id(3)
                .name("J3")
                .teamName("E2")
                .isGuardian(false)
                .build();
    }

    @Test
    // make test containing crupdate @Transactional to be more predictable
    @Transactional
    void create_players_ok() throws Exception {
        Player toCreate = Player.builder()
                .name("Joe Doe")
                .isGuardian(false)
                .teamName("E1")
                .build();

        RequestBuilder request = post("/players")
                .content(objectMapper.writeValueAsString(List.of(toCreate)))
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        List<Player> actual = convertFromHttpResponse(objectMapper, response, Player.class);

        assertEquals(1, actual.size());
        assertEquals(toCreate, actual.get(0).toBuilder().id(null).build());
    }

    @Test
    void create_players_ko() throws Exception {
        Player _toCreate = Player.builder()
                .id(100)
                .name("J100")
                .isGuardian(false)
                // non existing team name
                .teamName("E100")
                .build();

        RequestBuilder request = post("/players")
                .content(objectMapper.writeValueAsString(List.of(_toCreate)))
                .contentType(MediaType.APPLICATION_JSON);

        ServletException exception = assertThrows(ServletException.class, () -> mockMvc.perform(request));

        String exceptionMessage = normalizeKnownExceptionMessage(
                "Team" + _toCreate.getTeamName() + " not found.",
                HttpStatus.BAD_REQUEST
        );

        assertThrowsExceptionMessage(exceptionMessage, BadRequestException.class, () -> {
            throw exception.getRootCause();
        });
    }


    @Test
    // make test containing crupdate @Transactional to be more predictable
    @Transactional
    void edit_players_ok() throws Exception {
        Player toBeUpdated = player1().toBuilder()
                .name("JD1")
                .isGuardian(true)
                .build();

        RequestBuilder request = put("/players")
                .content(objectMapper.writeValueAsString(List.of(toBeUpdated)))
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        List<Player> actual = convertFromHttpResponse(objectMapper, response, Player.class);

        Player updated = actual.stream().filter(player -> player.getId() == toBeUpdated.getId()).findFirst().get();

        assertNotNull(updated);
        assertEquals(toBeUpdated, updated);
    }


    @Test
    void edit_players_ko() throws Exception {
        Player toBeUpdated = Player.builder()
                .id(NON_EXISTING_PLAYER_ID)
                .name("JMore")
                .isGuardian(false)
                .teamName("E1")
                .build();

        RequestBuilder request = put("/players")
                .content(objectMapper.writeValueAsString(List.of(toBeUpdated)))
                .contentType(MediaType.APPLICATION_JSON);

        ServletException exception = assertThrows(ServletException.class, () -> mockMvc.perform(request));

        String exceptionMessage = normalizeKnownExceptionMessage(
                "Player#" + toBeUpdated.getId() + " not found.",
                HttpStatus.NOT_FOUND
        );

        assertThrowsExceptionMessage(exceptionMessage, NotFoundException.class, () -> {
            throw exception.getRootCause();
        });

    }

    @Test
    void read_players_ok() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/players"))
                .andReturn()
                .getResponse();

        List<Player> actual = convertFromHttpResponse(objectMapper, response, Player.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertTrue(actual.containsAll(List.of(
                player1(),
                player2(),
                player3())));
    }
}
