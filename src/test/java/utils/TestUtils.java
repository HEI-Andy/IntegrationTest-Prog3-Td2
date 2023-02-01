package utils;

import app.foot.controller.rest.Match;
import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.rest.TeamMatch;
import app.foot.exception.ApiException;
import app.foot.model.Team;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUtils {

    public static PlayerScorer scorer1() {
        return PlayerScorer.builder()
                .player(player1())
                .isOG(false)
                .scoreTime(10)
                .build();
    }

    public static PlayerScorer nullScoreTimeScorer() {
        return scorer1().toBuilder()
                .scoreTime(null)
                .build();
    }

    public static Player player1() {
        return Player.builder()
                .id(1)
                .name("Rakoto")
                .isGuardian(false)
                .build();
    }

    public static app.foot.model.PlayerScorer rakotoModelScorer(app.foot.model.Player playerModelRakoto, PlayerScoreEntity scorerRakoto) {
        return app.foot.model.PlayerScorer.builder()
                .player(playerModelRakoto)
                .isOwnGoal(false)
                .minute(scorerRakoto.getMinute())
                .build();
    }

    public static Team teamModelGhana(TeamEntity teamEntityGhana) {
        return Team.builder()
                .id(teamEntityGhana.getId())
                .name(teamEntityGhana.getName())
                .build();
    }

    public static Team teamModelBarea(TeamEntity teamEntityBarea) {
        return Team.builder()
                .id(teamEntityBarea.getId())
                .name(teamEntityBarea.getName())
                .build();
    }

    public static PlayerScoreEntity scorerRakoto(PlayerEntity playerEntityRakoto) {
        return PlayerScoreEntity.builder()
                .id(1)
                .player(playerEntityRakoto)
                .minute(10)
                .build();
    }

    public static app.foot.model.Player playerModelRakoto(PlayerEntity playerEntityRakoto) {
        return app.foot.model.Player.builder()
                .id(playerEntityRakoto.getId())
                .name(playerEntityRakoto.getName())
                .isGuardian(false)
                .teamName(teamBarea().getName())
                .build();
    }

    public static PlayerEntity playerEntityRakoto(TeamEntity teamEntityBarea) {
        return PlayerEntity.builder()
                .id(1)
                .name("Rakoto")
                .guardian(false)
                .team(teamEntityBarea)
                .build();
    }

    public static TeamEntity teamGhana() {
        return TeamEntity.builder()
                .id(2)
                .name("Ghana")
                .build();
    }

    public static TeamEntity teamBarea() {
        return TeamEntity.builder()
                .id(1)
                .name("Barea")
                .build();
    }

    public static app.foot.model.Player playerModelWith_aNonExistingTeam() {
        return app.foot.model.Player.builder()
                .id(1)
                .teamName(nonExistingTeam().getName())
                .name("john_doe")
                .isGuardian(false)
                .build();
    }

    public static TeamEntity nonExistingTeam() {
        return TeamEntity.builder()
                .name("non_existing_team")
                .build();
    }


    public static Match match2() {
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

    public static app.foot.controller.rest.Team team3() {
        return app.foot.controller.rest.Team.builder()
                .id(3)
                .name("E3")
                .build();
    }

    public static app.foot.controller.rest.Team team1() {
        return app.foot.controller.rest.Team.builder()
                .id(1)
                .name("E1")
                .build();
    }

    public static app.foot.controller.rest.Team team2() {
        return app.foot.controller.rest.Team.builder()
                .id(2)
                .name("E2")
                .build();
    }

    public static Player player6() {
        return Player.builder()
                .id(6)
                .name("J6")
                .teamName("E3")
                .isGuardian(false)
                .build();
    }

    public static Player player3() {
        return Player.builder()
                .id(3)
                .name("J3")
                .teamName("E2")
                .isGuardian(false)
                .build();
    }



    public static void assertThrowsExceptionMessage(String message, Class exceptionClass, Executable executable) {
        Throwable exception = assertThrows(exceptionClass, executable);
        assertEquals(message, exception.getMessage());
    }

    public static <T> List<T> convertFromHttpResponse(ObjectMapper mapper, MockHttpServletResponse response, Class<T> klass)
            throws JsonProcessingException, UnsupportedEncodingException {
        CollectionType classListType = mapper.getTypeFactory()
                .constructCollectionType(List.class, klass);
        return mapper.readValue(
                response.getContentAsString(),
                classListType);
    }

    public static String normalizeKnownExceptionMessage(String message, HttpStatus status) {
        if (status != null) {
            return status.toString() + " : " + message;
        } return message;
    }
}
