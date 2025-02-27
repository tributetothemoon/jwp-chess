package chess.service;

import chess.domain.piece.Color;
import chess.dto.NewGameDto;
import chess.dto.RunningGameDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ChessGameServiceTest {
    @Autowired
    private ChessGameService chessGameService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("새로운 게임을 생성한다.")
    void createNewGameTest() {
        // when
        NewGameDto newGameDto = chessGameService.createNewGame("test title");

        // then
        assertThat(newGameDto.getChessBoard()).isInstanceOf(Map.class);
        assertThat(newGameDto.getCurrentTurnColor()).isInstanceOf(Color.class);
    }

    @Test
    @DisplayName("게임의 고유 값으로 게임을 읽어온다.")
    void loadChessGameBByGameId() {
        // given
        long gameId = chessGameService.createNewGame("test title").getGameId();

        // when
        RunningGameDto runningGameDto = chessGameService.loadChessGame(gameId);

        // then
        assertThat(runningGameDto).isNotNull();
        assertThat(runningGameDto).isInstanceOf(RunningGameDto.class);
    }

    @Test
    @DisplayName("기물을 이동한 결과를 반환한다.")
    void moveTest() {
        // given
        long gameId = chessGameService.createNewGame("test title").getGameId();

        // when
        RunningGameDto runningGameDto = chessGameService.move(gameId, "a2", "a4");

        // then
        assertThat(runningGameDto.getChessBoard().get("a4")).isNotNull();
        assertThat(runningGameDto.getCurrentTurnColor()).isEqualTo(Color.BLACK);
    }

    @Test
    @DisplayName("게임들의 목록을 읽어온다.")
    void loadAllGamesTest() {
        int NUMBER_OF_GAME = 3;
        for (int i = 0; i < NUMBER_OF_GAME; i++) {
            chessGameService.createNewGame("test title");
        }

        assertThat(chessGameService.loadAllGames().getGames()).hasSize(NUMBER_OF_GAME);
    }

    @AfterEach
    void flush() {
        jdbcTemplate.execute("DELETE FROM game");
    }
}