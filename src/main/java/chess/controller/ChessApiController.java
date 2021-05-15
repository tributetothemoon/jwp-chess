package chess.controller;

import chess.dto.*;
import chess.exception.InvalidGameIdRangeException;
import chess.exception.NullTitleException;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class ChessApiController {
    private final ChessGameService chessGameService;

    public ChessApiController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/games")
    public ResponseEntity<CommonResponse<NewGameDto>> newGame(@RequestBody CreateGameRequest createGameRequest) {
        validateTitleIsNotNull(createGameRequest.getTitle());

        NewGameDto newGameDto = chessGameService.createNewGame(createGameRequest.getTitle());

        return ResponseEntity.created(URI.create("/games/" + newGameDto.getGameId()))
                .body(new CommonResponse<>(
                        "새로운 게임이 생성되었습니다.",
                        newGameDto));
    }

    private void validateTitleIsNotNull(String title) {
        if (!StringUtils.hasText(title)) {
            throw new NullTitleException("게임 제목이 없습니다.");
        }
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<CommonResponse<RunningGameDto>> loadGame(@PathVariable long gameId) {
        validateGameIdRange(gameId);
        return ResponseEntity.ok(
                new CommonResponse<>(
                        "게임을 불러왔습니다",
                        chessGameService.loadChessGame(gameId)));
    }

    private void validateGameIdRange(long gameId) {
        if (gameId <= 0) {
            throw new InvalidGameIdRangeException("유효하지 않은 범위의 game id 값을 요청했습니다.");
        }
    }

    @PutMapping("/games/{gameId}/pieces")
    public ResponseEntity<CommonResponse<RunningGameDto>> move(@PathVariable long gameId, @RequestBody MoveRequest moveRequest) {
        String from = moveRequest.getFrom();
        String to = moveRequest.getTo();

        return ResponseEntity.ok(
                new CommonResponse<>(
                        "기물을 이동했습니다.",
                        chessGameService.move(gameId, from, to)));
    }

    @GetMapping("/games")
    public ResponseEntity<CommonResponse<GameListDto>> loadGames() {
        return ResponseEntity.ok(
                new CommonResponse<>(
                        "게임 목록을 불러왔습니다.",
                        chessGameService.loadAllGames()));
    }
}
