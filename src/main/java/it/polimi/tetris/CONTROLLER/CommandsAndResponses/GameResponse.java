package it.polimi.tetris.CONTROLLER.CommandsAndResponses;

public class GameResponse extends Response {


    public GameResponse() {
    }

    public GameResponse(String message, String description, String nickname) {
        super(message, description, nickname);
    }
}
