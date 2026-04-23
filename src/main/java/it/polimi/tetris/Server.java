package it.polimi.tetris;

import it.polimi.tetris.CONTROLLER.ClientHandler;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.GameResponse;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.Response;
import it.polimi.tetris.MODEL.ENUMS.LobbyStatus;
import it.polimi.tetris.MODEL.ENUMS.PlayerColor;
import it.polimi.tetris.MODEL.Game;
import it.polimi.tetris.MODEL.Lobby;
import it.polimi.tetris.MODEL.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Server {

    private ArrayList<ClientHandler> clients; //list of clienthandler, one per player
    private ArrayList<Lobby> lobbies; //list of all lobbyes started and ongoing in the server
    private int lobbyIndex; //number assigned at the lobbyId
    private ArrayList<Game> activeGames; //list of started games

    public Server() {
        this.clients = new ArrayList<>();
        this.lobbies = new ArrayList<>();
        this.activeGames = new ArrayList<>();
    }

    public void AddClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public void RemoveClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public void AddLobby(Lobby lobby) {
        lobbies.add(lobby);
    }

    public void RemoveLobby(Lobby lobby) {
        lobbies.remove(lobby);
    }

    public ArrayList<Lobby> GetAvailableLobbies() {

        // ritorna solo le lobby in attesa di giocatori
        return lobbies.stream()
                .filter(l -> l.getStatus() == LobbyStatus.WAITING)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void setClients(ArrayList<ClientHandler> clients) {
        this.clients = clients;
    }

    public void setLobbies(ArrayList<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    public int getLobbyIndex() {
        return lobbyIndex;
    }

    public void setLobbyIndex(int lobbyIndex) {
        this.lobbyIndex = lobbyIndex;
    }

    public ArrayList<ClientHandler> getClients() { return clients; }

    public ArrayList<Lobby> getLobbies() { return lobbies; }

    public ArrayList<Game> getActiveGames() {
        return activeGames;
    }

    public void setActiveGames(ArrayList<Game> activeGames) {
        this.activeGames = activeGames;
    }

    public void AddGame(Game game) {
        activeGames.add(game);
        game.setOnGameEnd(() -> {
            Player[] ranking = game.getRanking();
            ArrayList<String> rankingInfo = new ArrayList<>();
            int pos = 1;
            for (Player p : ranking) {
                if (p == null) continue;
                String colorHex = PlayerColorToHex(p.getPlayerColor());
                rankingInfo.add(pos + "|" + p.getNickname() + " - " + p.getTetrisMatch().getScore() + "|" + colorHex);
                pos++;
            }
            GameResponse endResponse = new GameResponse("GAME_ENDED", "", "");
            endResponse.setRankingInfo(rankingInfo);
            for (Lobby lo : getLobbies())
                if (lo.getLobbyId() == game.getGameID()) {
                    SendToAll(endResponse, lo);
                    break;
                }
        });
    }

    private String PlayerColorToHex(PlayerColor color) {
        if (color == null) return "#ffffff";
        return switch (color) {
            case LIGHTBLUE -> "#00bfff";
            case RED -> "#ff4455";
            case PURPLE -> "#aa44ff";
            case LIGHTGREEN -> "#44ff88";
        };
    }

    public void SendToAll(Response response, Lobby lobby) {
        for (ClientHandler ch : clients) {
            if (lobby.getPlayers().contains(ch.getPlayer())) {
                ch.SendResponse(response);
            }
        }
    }
}