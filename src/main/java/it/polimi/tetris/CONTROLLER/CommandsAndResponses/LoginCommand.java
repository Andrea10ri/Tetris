package it.polimi.tetris.CONTROLLER.CommandsAndResponses;

import it.polimi.tetris.MODEL.ENUMS.DurationTime;
import it.polimi.tetris.MODEL.ENUMS.PlayerColor;

public class LoginCommand extends Command{

    private int numOfPlayers;
    private DurationTime duration;


    // Fields
    public LoginCommand( String command, String nickname ) {
        super(command,nickname);
    }

    public LoginCommand() {
        super();
    }

    public LoginCommand(String CommandName, String Nickname, int numOfPlayers, DurationTime duration) {
        super(CommandName, Nickname);
        this.numOfPlayers = numOfPlayers;
        this.duration = duration;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public DurationTime getDuration() {
        return duration;
    }

    public void setDuration(DurationTime duration) {
        this.duration = duration;
    }
}
