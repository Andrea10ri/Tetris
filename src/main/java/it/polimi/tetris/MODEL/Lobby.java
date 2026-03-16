package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUMS.CellStatus;
import it.polimi.tetris.MODEL.ENUMS.DurationTime;
import it.polimi.tetris.MODEL.ENUMS.LobbyStatus;

import java.util.ArrayList;

public class Lobby {

    //attributes
    private int numOfPlayers;
    private final int MIN_PLAYERS = 2;
    private final int MAX_PLAYERS = 4;
    private ArrayList<Player> players;
    private DurationTime durationTime;
    private String lobbyId;        // identificativo univoco della lobby
    private Player host;           // chi ha creato la lobby
    private LobbyStatus status;    // WAITING, FULL, IN_GAME

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

    //METHODS

    public void AddPlayer(Player player) {
        if (!IsFull())
            players.add(player);
    }

    public void RemovePlayer(Player player) {
        players.remove(player);
    }

    public boolean IsFull() {
        return players.size() >= numOfPlayers;
    }

    public Game StartGame() {
        status = LobbyStatus.IN_GAME;

        for (Player player : players) {
            //inizializza la griglia con celle vuote
            Cell[][] grid = new Cell[20][10];
            for (int r = 0; r < 20; r++)
                for (int c = 0; c < 10; c++)
                    grid[r][c] = new Cell(null, null, CellStatus.EMPTY);

            // crea la board e il match per il giocatore
            TetrisBoard tb = new TetrisBoard(10, 20, grid);
            TetrisMatch t = new TetrisMatch(tb, 1, 0);
            player.setTetrisMatch(t);
        }

        // crea il Game e lo avvia
        Game game = new Game(numOfPlayers, players, durationTime);
        game.Start();
        return game;
    }
}
