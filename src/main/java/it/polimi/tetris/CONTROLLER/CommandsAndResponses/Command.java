package it.polimi.tetris.CONTROLLER.CommandsAndResponses;

public class Command {


    // Fields
    protected String commandName;  // Name of the command to do
    protected String nickname; // Nickname of the player

    // Constructors
    public Command(){
        this.commandName = "";
        this.nickname = "";
    }

    public Command(String CommandName, String Nickname) {
        this.commandName = CommandName;
        this.nickname = Nickname;
    }

    // Getters and setters
    public String getCommandName() {
        return this.commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
