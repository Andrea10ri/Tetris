package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUM.DurationTime;
import it.polimi.tetris.MODEL.ENUM.GameStatus;

import java.util.ArrayList;
import java.util.Timer;

public class Game {

    //attributes
    private int numOfPlayers; //number of players
    private ArrayList<Player> Players; //array list of players
    private Timer remainingTime; //timer
    private DurationTime durationTime; // time of the match
    private Player [] ranking; //ranking of the match
    private GameStatus gameStatus; //status of the game

    //constructor
    public Game(int numOfPlayers, ArrayList<Player> players, Timer remainingTime, DurationTime durationTime, Player[] ranking, GameStatus gameStatus) {
        this.numOfPlayers = numOfPlayers;
        Players = players;
        this.remainingTime = remainingTime;
        this.durationTime = durationTime;
        this.ranking = ranking;
        this.gameStatus = gameStatus;
    }

    //getter and setter
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return Players;
    }

    public void setPlayers(ArrayList<Player> players) {
        Players = players;
    }

    public Timer getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Timer remainingTime) {
        this.remainingTime = remainingTime;
    }

    public DurationTime getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(DurationTime durationTime) {
        this.durationTime = durationTime;
    }

    public Player[] getRanking() {
        return ranking;
    }

    public void setRanking(Player[] ranking) {
        this.ranking = ranking;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
