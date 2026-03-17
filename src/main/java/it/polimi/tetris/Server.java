package it.polimi.tetris;

import it.polimi.tetris.CONTROLLER.ClientHandler;
import it.polimi.tetris.MODEL.ENUMS.LobbyStatus;
import it.polimi.tetris.MODEL.Lobby;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Server {

    private ArrayList<ClientHandler> clients; //list of clienthandler, one per player
    private ArrayList<Lobby> lobbies; //list of all lobbyes started and ongoing in the server

    public Server() {
        this.clients = new ArrayList<>();
        this.lobbies = new ArrayList<>();
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

    public ArrayList<ClientHandler> getClients() { return clients; }
    public ArrayList<Lobby> getLobbies() { return lobbies; }
}