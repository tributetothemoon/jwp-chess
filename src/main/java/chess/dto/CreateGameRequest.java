package chess.dto;

import javax.validation.constraints.NotEmpty;

public class CreateGameRequest {
    @NotEmpty
    private String title;

    public CreateGameRequest() {
    }

    public CreateGameRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
