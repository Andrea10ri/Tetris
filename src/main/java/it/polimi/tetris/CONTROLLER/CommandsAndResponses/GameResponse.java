package it.polimi.tetris.CONTROLLER.CommandsAndResponses;

import it.polimi.tetris.MODEL.ENUMS.PlayerColor;
import it.polimi.tetris.MODEL.Player;

import java.util.ArrayList;

public class GameResponse extends Response {

    private int remainingTime;
    private int phase;
    private ArrayList<String> enemiesNicknames;
    private PlayerColor playerColor;

    private int[][] board;
    private int[][] currentShape;
    private int currentX;
    private int currentY;
    private int currentColor;
    private int ghostY;
    private int[][] nextShape;
    private int nextColor;
    private int score;
    private boolean gameOver;



    public GameResponse() {}

    public GameResponse(String message, String description, String nickname) {
        super(message, description, nickname);
    }

    public GameResponse(String message, String description, String nickname, int remainingTime, int phase, ArrayList<String> enemiesNicknames, PlayerColor playerColor) {
        super(message, description, nickname);
        this.remainingTime = remainingTime;
        this.phase = phase;
        this.enemiesNicknames = enemiesNicknames;
        this.playerColor = playerColor;
    }


    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public ArrayList<String> getEnemiesNicknames() {
        return enemiesNicknames;
    }

    public void setEnemiesNicknames(ArrayList<String> enemiesNicknames) {
        this.enemiesNicknames = enemiesNicknames;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int[][] getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(int[][] currentShape) {
        this.currentShape = currentShape;
    }

    public int getCurrentX() {
        return currentX;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }

    public int getGhostY() {
        return ghostY;
    }

    public void setGhostY(int ghostY) {
        this.ghostY = ghostY;
    }

    public int[][] getNextShape() {
        return nextShape;
    }

    public void setNextShape(int[][] nextShape) {
        this.nextShape = nextShape;
    }

    public int getNextColor() {
        return nextColor;
    }

    public void setNextColor(int nextColor) {
        this.nextColor = nextColor;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isGameOver() { return gameOver; }

    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    //classe interna usata solo per supporto
    public static class OpponentBoard {
        private String nickname;
        private int[][] board;
        private int score;

        public OpponentBoard(String nickname, int[][] board, int score) {
            this.nickname = nickname;
            this.board = board;
            this.score = score;
        }

        public String getNickname() { return nickname; }
        public int[][] getBoard() { return board; }
        public int getScore() { return score; }
    }

    private ArrayList<OpponentBoard> opponentBoards;

    public ArrayList<OpponentBoard> getOpponentBoards() { return opponentBoards; }

    public void setOpponentBoards(ArrayList<OpponentBoard> opponentBoards) { this.opponentBoards = opponentBoards;
    }

}
