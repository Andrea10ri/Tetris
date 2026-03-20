package it.polimi.tetris.CONTROLLER.CommandsAndResponses;

public class Response {


    // Fields
    protected String message; // Message that indicates the result of the operation
    protected String description; // Description of the message
    protected String nickname;  // Nickname of the player

    // Constructors
    public Response() {
        this.message = "";
        this.description = "";
        this.nickname = "";
    }

    public Response(String message, String description, String nickname) {
        this.message = message;
        this.description = description;
        this.nickname = nickname;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
