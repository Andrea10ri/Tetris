package it.polimi.tetris.CONTROLLER;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.*;
import it.polimi.tetris.MODEL.ENUMS.LobbyStatus;
import it.polimi.tetris.MODEL.Lobby;
import it.polimi.tetris.MODEL.Player;
import it.polimi.tetris.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {

    private Player player;
    private int lobbyID;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Socket clientSocket; //socket used by the client
    private Server server;
    private Response response;  //Response to send to the client
    private Command command;    //Command received from client
    private Gson gson; //gson for json communication


    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.in=null;
        this.out=null;
        GsonBuilder builder = new GsonBuilder();
       // builder.registerTypeHierarchyAdapter(Tile.class, new TileTypeAdapter());
       // builder.registerTypeHierarchyAdapter(AdventureCard.class, new CardsTypeAdapter());
        this.gson = builder.create();

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    private void BufferInstance() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    //Running all the phases of the game, starting from the login and processing to the end of the game
    public void run() {


        //connection
        BufferInstance();
        System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

        //Login phase
        LoginLoop();
        //Game phase
        GameLoop();





    }


    //Loop that mamnage the messages obtained during the login phase
    private void LoginLoop() {

        //command received
        command=new LoginCommand("","");

       //input string
        String message="";
        response=new LoginResponse();

        while(!clientSocket.isClosed() && !command.getCommandName().equals("FinishLogin"))
        {
            try {
                message = in.readLine();
                System.out.println("Received: " + message);
                // Create the Command from json string
                command = gson.fromJson(message, LoginCommand.class);
                // Processing the command
                LoginCommandProcess((LoginCommand) command);
            }
            catch (IOException e) {
                System.out.println("Client disconnected");
                return;
            }
        }
    }

    /**
     * Processing the login command in input
     * @param cmd Command to process
     */
    public void LoginCommandProcess(LoginCommand cmd) {

        switch (cmd.getCommandName()) {

            case "NickName_Insert":
                //Player instance
                player = new Player();
                // Set nickname
                player.setNickname(cmd.getNickname());

                //Response
                response = new LoginResponse("LOGIN OK", "Login successfully",cmd.getNickname());
                SendResponse(response);
             break;

            case "Create_Lobby":

                ArrayList<Player> players = new ArrayList<>();
                players.add(player);
                Lobby l = new Lobby(cmd.getNumOfPlayers(), players, cmd.getDuration(), player.getNickname());
                l.setLobbyId(server.getLobbyIndex());
                lobbyID = server.getLobbyIndex();
                l.setStatus(LobbyStatus.WAITING);
                server.setLobbyIndex(server.getLobbyIndex()+1);
                server.AddLobby(l);

                //Response
                response = new LoginResponse("LOBBY CREATED", "Lobby created successfully",cmd.getNickname(), l);
                SendResponse(response);

                break;

            case "Join_Lobby":

               //devo cercare la lobby tra la lista delle lobby e aggiungere il player a quella lobby
                for (Lobby lo : server.getLobbies()) {

                    if (lo.getLobbyId() == cmd.getSelectedLobby().getLobbyId() ) {

                        //lobby non piena
                        if(lo.getPlayers().size() < lo.getNumOfPlayers()){
                        lo.AddPlayer(player);
                        response = new LoginResponse("LOBBY JOINED", "Joined successfully", cmd.getNickname(), lo);
                        SendResponse(response);
                        lobbyID=lo.getLobbyId();

                        //lobby riempita
                            if(lo.getPlayers().size() == lo.getNumOfPlayers())
                            {
                                lo.setStatus(LobbyStatus.FULL);
                            }
                        // notifica tutti gli altri
                        LoginResponse update = new LoginResponse("LOBBY UPDATED", "New player joined", "", lo);
                        server.SendToAll(update, lo);

                        break;
                        }

                        //lobby piena
                        else
                        {
                            response = new LoginResponse("FULL LOBBY", "The selected lobby is full", cmd.getNickname(), lo);
                            SendResponse(response);
                        }


                    }


                }

                break;

            case "GetLobbies":

                //Response
                response = new LoginResponse("LOBBY LIST", "Updated lobby list",cmd.getNickname(), server.getLobbies());
                SendResponse(response);

                break;


            case "PlayerReady":

                         Lobby lo = getMyLobby();

                        //colore disponibile
                        if(lo.getAvailableColors().contains(cmd.getSelectedColor()) )
                        {
                            this.player.setPlayerColor(cmd.getSelectedColor());

                            // Response
                            response=new LoginResponse("COLOR OK", "Color set correctly", this.player.getNickname(), lo,cmd.getSelectedColor());

                            // Send Response
                            SendResponse(response);

                            response = new LoginResponse("LOBBY UPDATED", "Refreshing the view", this.player.getNickname(), lo);
                            server.SendToAll(response, lo);


                               //checking if this client is it or not the last on
                              if((4 - lo.getAvailableColors().size()) == lo.getNumOfPlayers())
                                  {
                                      response = new LoginResponse("LAST PLAYER ENTERED", "Lobby is full, the game can start", this.player.getNickname(), lo);
                                      server.SendToAll(response, lo);
                                  }
                        }

                        //colore non disponibile
                        else
                        {
                            // Response
                            response=new LoginResponse("COLOR NOT OK", "Color already taken ", this.player.getNickname(), lo);

                            // Send Response
                            SendResponse(response);
                        }




                break;

            case "FinishLogin":

                //waiting the countdown to finish
                try {
                    TimeUnit.MILLISECONDS.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //setting the game
                if(isHost())
                {
                  Lobby lob =  getMyLobby();
                  server.AddGame(lob.StartGame());
                }


                response=new LoginResponse("FINISH LOGIN","", this.player.getNickname());

                // Send Response
                SendResponse(response);
                break;

        }
    }

    //Loop that mamnage the messages obtained during the game phase
    private void GameLoop() {

        //command received
        command = new GameCommand();

        //input string
        String message="";
        response=new GameResponse();

        while(!clientSocket.isClosed() && !command.getCommandName().equals("FinishGame"))
        {
            try {
                message = in.readLine();
                System.out.println("Received: " + message);
                //Create the Command from json string
                command = gson.fromJson(message, LoginCommand.class);
                // Processing the command
                GameCommandProcess((GameCommand) command);
            }
            catch (IOException e) {
                System.out.println("Client disconnected");
                return;
            }
        }

    }


    /**
     * Processing the game command in input
     * @param cmd Command to process
     */
    public void GameCommandProcess(GameCommand cmd) {


        switch (cmd.getCommandName()) {

        }
    }


    public void Send(String message) {
        out.println(message);
    }

    /**
     * Send the response to client
     * @param response Response to send to client
     */
    public void SendResponse(Response response) {

        this.out.println(gson.toJson(response));
        this.out.flush();

    }

    public boolean isHost(){

        for (Lobby lo : server.getLobbies()) {


            if (lo.getLobbyId() == lobbyID && lo.getPlayers().getFirst().getNickname().equals(this.player.getNickname())) {

                return true;

            }

        }
        return false;
    }

    public Lobby getMyLobby() {

        for (Lobby lo : server.getLobbies()) {


            if (lo.getLobbyId() == lobbyID){
                return lo;
            }

        }
        return null;
    }
}