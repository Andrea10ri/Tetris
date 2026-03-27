package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUMS.CellStatus;
import it.polimi.tetris.MODEL.ENUMS.DurationTime;
import it.polimi.tetris.MODEL.ENUMS.LobbyStatus;
import it.polimi.tetris.MODEL.ENUMS.PlayerColor;

import java.util.ArrayList;

public class Lobby {

    //attributes
    private int numOfPlayers;
    private final int MIN_PLAYERS = 2;
    private final int MAX_PLAYERS = 4;
    private ArrayList<Player> players;
    private DurationTime durationTime;
    private int lobbyId;        // identificativo univoco della lobby
    private String host;           // chi ha creato la lobby
    private LobbyStatus status;    // WAITING, FULL, IN_GAME

    public Lobby(int numOfPlayers, ArrayList<Player> players, DurationTime durationTime, String host) {
        this.numOfPlayers = numOfPlayers;
        this.players = players;
        this.durationTime = durationTime;
        this.host = host;
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

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public void setStatus(LobbyStatus status) {
        this.status = status;
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



    /**
     * <p>Get the list of available colors to select in login phase</p>
     * @return list of available colors if there is at least one color, null otherwise
     */
    public ArrayList<PlayerColor> getAvailableColors() {

        // List of Player Color
        ArrayList<PlayerColor> AvailableColors = new ArrayList<>();
        AvailableColors.add(PlayerColor.LIGHTBLUE);
        AvailableColors.add(PlayerColor.LIGHTGREEN);
        AvailableColors.add(PlayerColor.RED);
        AvailableColors.add(PlayerColor.PURPLE);

        //Search color already used
        for(Player p : players) {
            if(AvailableColors.contains(p.getPlayerColor())) {
                AvailableColors.remove(p.getPlayerColor());
            }
        }

        //If available colors is empty return null
        if(AvailableColors.isEmpty()) {
            return null;
        }

        return  AvailableColors;
    }
}
