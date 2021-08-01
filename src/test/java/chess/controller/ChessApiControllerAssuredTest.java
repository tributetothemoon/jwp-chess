package chess.controller;

import chess.dto.CreateGameRequest;
import chess.dto.MoveRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ChessApiControllerAssuredTest {
    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ObjectMapper objectMapper;
    private RandomGameIdGenerator randomGameIdGenerator = new RandomGameIdGenerator();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        this.objectMapper = new ObjectMapper();
    }

    @DisplayName("게임 생성 요청")
    @Test
    void createGameRequestTest() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(new CreateGameRequest("test title"));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(content)
                .when().post("/games")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }



    @DisplayName("기물 이동 요청시 유효하지 않은 좌표 값이 들어오면 Bad Request로 응답한다.")
    void movePieceApiPositionValidationTest() throws Exception {
        long gameId = randomGameIdGenerator.generateValidRandomGameId();

        String content = objectMapper.writeValueAsString(new MoveRequest("aaa", "111"));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(content)
                .when().put("/games/" + gameId + "/pieces")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private static class RandomGameIdGenerator {
        private static final long LOWER_BOUND = 1;
        private static final long UPPER_BOUND = Long.MAX_VALUE;

        long generateValidRandomGameId() {
            return ThreadLocalRandom.current().nextLong(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
        }
    }

    @AfterEach
    void flush() {
        jdbcTemplate.execute("DELETE from game");
    }
}
