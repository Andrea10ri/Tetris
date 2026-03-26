package it.polimi.tetris.CONTROLLER.CommandsAndResponses;

import it.polimi.tetris.MODEL.Lobby;

import java.util.ArrayList;

public class LoginResponse extends Response{


    private ArrayList<Lobby> lobbies;
    private Lobby selectedLobby;

    public LoginResponse() {
        super();
    }

    public LoginResponse(String message, String description, String nickname) {

        super(message, description, nickname);
    }

    public LoginResponse(String message, String description, String nickname, ArrayList<Lobby> lobbies) {
        super(message, description, nickname);
        this.lobbies = lobbies;
    }

    public LoginResponse(String message, String description, String nickname, Lobby selectedLobby) {
        super(message, description, nickname);
        this.selectedLobby = selectedLobby;
    }

    public ArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    public void setLobbies(ArrayList<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    public Lobby getSelectedLobby() {
        return selectedLobby;
    }

    public void setSelectedLobby(Lobby selectedLobby) {
        this.selectedLobby = selectedLobby;
    }
}
