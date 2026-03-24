package it.polimi.tetris.CONTROLLER;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.Command;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.LoginCommand;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.LoginResponse;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.Response;
import it.polimi.tetris.MODEL.Lobby;
import it.polimi.tetris.MODEL.Player;
import it.polimi.tetris.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Player player;
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



    @Override
    //Running all the phases of the game, starting from the login and processing to the end of the game
    public void run() {


        //connection
        BufferInstance();
        System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

        //phases
        LoginLoop();




    }

    private void BufferInstance() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                server.AddLobby(l);

                //Response
                response = new LoginResponse("LOBBY CREATED", "Lobby created successfully",cmd.getNickname());
                SendResponse(response);

                break;

            case "GetLobbies":

                //Response
                response = new LoginResponse("LOBBY LIST", "Updated lobby list",cmd.getNickname(), server.getLobbies());
                SendResponse(response);

                break;

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
}