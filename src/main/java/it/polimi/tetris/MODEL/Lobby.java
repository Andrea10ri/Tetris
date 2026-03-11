package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUMS.DurationTime;
import java.util.ArrayList;

public class Lobby {

    //attributes
    private int numOfPlayers;
    private final int MIN_PLAYERS = 2;
    private final int MAX_PLAYERS = 4;
    private ArrayList<Player> players;
    private DurationTime durationTime;

    public Lobby(int numOfPlayers, ArrayList<Player> players, DurationTime durationTime) {
        this.numOfPlayers = numOfPlayers;
        this.players = players;
        this.durationTime = durationTime;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public int getMIN_PLAYERS() {
        return MIN_PLAYERS;
    }

    public int getMAX_PLAYERS() {
        return MAX_PLAYERS;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public DurationTime getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(DurationTime durationTime) {
        this.durationTime = durationTime;
    }
}
