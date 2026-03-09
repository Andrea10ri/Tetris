package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUM.PlayerColor;

public class Player {

    //attributes
    private String nickname;
    private PlayerColor playerColor;
    private TetrisBoard tetrisBoard;
    private int score;

    //constructor
    public Player(String nickname, PlayerColor playerColor, TetrisBoard tetrisBoard, int score) {
        this.nickname = nickname;
        this.playerColor = playerColor;
        this.tetrisBoard = tetrisBoard;
        this.score = score;
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

    public TetrisBoard getTetrisBoard() {
        return tetrisBoard;
    }

    public void setTetrisBoard(TetrisBoard tetrisBoard) {
        this.tetrisBoard = tetrisBoard;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
