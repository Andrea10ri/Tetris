package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUMS.PlayerColor;

public class Player {

    //attributes
    private String nickname;
    private PlayerColor playerColor;
    private TetrisMatch tetrisMatch;


    //constructor
    public Player () {
        this.nickname = null;
        this.playerColor = null;
        this.tetrisMatch = null;
    }
    public Player(String nickname, PlayerColor playerColor, TetrisMatch tetrisMatch) {
        this.nickname = nickname;
        this.playerColor = playerColor;
        this.tetrisMatch = tetrisMatch;

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public TetrisMatch getTetrisMatch() {
        return tetrisMatch;
    }

    public void setTetrisMatch(TetrisMatch tetrisMatch) {
        this.tetrisMatch = tetrisMatch;
    }

}
