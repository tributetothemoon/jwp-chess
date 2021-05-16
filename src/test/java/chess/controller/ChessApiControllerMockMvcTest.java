package chess.controller;


import chess.dto.CreateGameRequest;
import chess.dto.MoveRequest;
import chess.service.ChessGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class ChessApiControllerMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessGameService chessGameService;

    @Autowired
    private ObjectMapper objectMapper;

    private RandomGameIdGenerator randomGameIdGenerator = new RandomGameIdGenerator();
    private RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator();

    @DisplayName("게임 생성 시 빈 제목의 게임을 생성 요청할 경우 400 코드를 응답한다.")
    @Test
    void receiveBadRequestOnRequestEmptyTitleGame() throws Exception {
        String content = objectMapper.writeValueAsString(new CreateGameRequest(""));

        this.mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("게임 목록 불러오기 [GET] /games의 정상 작동")
    @Test
    void requestGamesTest() throws Exception {
        this.mockMvc.perform(get("/games"))
                .andExpect(status().isOk());
    }

    @DisplayName("기물 이동 요청 API의 정상 작동")
    @Test
    void movePieceTest() throws Exception {
        long gameId = randomGameIdGenerator.generateValidRandomGameId();

        String fromPosition = randomPositionGenerator.generateRandomPosition();
        String toPosition = randomPositionGenerator.generateRandomPosition();

        String content = objectMapper.writeValueAsString(new MoveRequest(fromPosition, toPosition));
        
        this.mockMvc.perform(put("/games/" + gameId +  "/pieces")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }

    @DisplayName("기물 이동 요청 시 좌표 값이 누락되면 Bad Request로 응답한다.")
    @Test
    void movePieceParameterValidationTest() throws Exception {
        long gameId = randomGameIdGenerator.generateValidRandomGameId();

        String content = objectMapper.writeValueAsString(new MoveRequest(null, null));

        ResultActions perform = this.mockMvc.perform(put("/games/" + gameId + "/pieces")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        perform.andExpect(status().isBadRequest());
        perform.andExpect(jsonPath("$.causes.from").value("must not be empty"));
        perform.andExpect(jsonPath("$.causes.to").value("must not be empty"));
    }

    @DisplayName("특정 게임 불러오기 요청 API 테스트")
    @Test
    void loadGameTest() throws Exception {
        this.mockMvc.perform(get("/games/" + randomGameIdGenerator.generateValidRandomGameId()))
                .andExpect(status().isOk());
    }

    @DisplayName("유효할 수 없는 게임의 고유값이 요청되면 Bad Request를 응답한다.")
    @Test
    void invalidGameRequest() throws Exception {
        long gameId = randomGameIdGenerator.generateInvalidRandomGameId();

        this.mockMvc.perform(get("/games/" + gameId))
                .andExpect(status().isBadRequest());
    }

    private static class RandomGameIdGenerator {
        private static final long LOWER_BOUND = 1;
        private static final long UPPER_BOUND = Long.MAX_VALUE;

        long generateValidRandomGameId() {
            return ThreadLocalRandom.current().nextLong(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
        }

        long generateInvalidRandomGameId() {
            return (-1) * ThreadLocalRandom.current().nextLong(UPPER_BOUND);
        }
    }

    private static class RandomPositionGenerator {
        private static final String FILES = "abcdefgh";
        private static final int RANK_MIN = 1;
        private static final int RANK_MAX = 8;

        private final Random random;

        public RandomPositionGenerator() {
            this.random = new Random();
        }

        private int generateRandomInt() {
            return random.nextInt(RandomPositionGenerator.RANK_MAX - RandomPositionGenerator.RANK_MIN) + RandomPositionGenerator.RANK_MIN;
        }

        public String generateRandomPosition() {
            return Character.toString(FILES.charAt(generateRandomInt())) + generateRandomInt();
        }

    }
}
