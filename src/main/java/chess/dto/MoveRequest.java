package chess.dto;

import javax.validation.constraints.NotEmpty;

public class MoveRequest {
    @NotEmpty
    private String from;
    @NotEmpty
    private String to;

    public MoveRequest() {
    }

    public MoveRequest(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
