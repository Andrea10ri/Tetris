package it.polimi.tetris.CONTROLLER;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.*;
import it.polimi.tetris.MODEL.*;
import it.polimi.tetris.MODEL.ENUMS.GameStatus;
import it.polimi.tetris.MODEL.ENUMS.LobbyStatus;
import it.polimi.tetris.MODEL.ENUMS.TetronimoColor;
import it.polimi.tetris.MODEL.Effects.*;
import it.polimi.tetris.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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


    //Loop that manage the messages obtained during the login phase
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

    //Loop that manage the messages obtained during the game phase
    private void GameLoop() {

        //aspetta che il game esista
        Game g = null;
        while (g == null) {
            g = getMyGame();
            if (g == null) {
                try { Thread.sleep(100); }
                catch (InterruptedException e) { return; }
            }
        }

        //info iniziali
        response = new GameResponse("SET GAME", "", player.getNickname(), g.getRemainingTime(), g.getGamePhase(),getEnemiesNicks(), this.player.getPlayerColor());
        SendResponse(response);

        //timer del tick
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {


            @Override
            public void run() {
                Game game = getMyGame();
                if (game == null || game.getStatus() == GameStatus.ENDED) {
                    timer.cancel();
                    return;
                }

                if (player.getTetrisMatch().getTetrisBoard().IsGameOver()) {
                    response=new GameResponse("FINISH GAME","", "");

                    // Send Response
                    SendResponse(response);
                }

                //tick sul match del giocatore
                player.getTetrisMatch().Tick(false);
                // manda lo stato aggiornato
                SendGameState();
            }
        }, 250, 250);

        //timer del tempo
        Timer gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Game game = getMyGame();
                if (game == null || game.getStatus() == GameStatus.ENDED) {
                    gameTimer.cancel();
                    return;
                }
                // solo l'host gestisce il timer globale
                if (isHost()) {
                    game.TickTimer();
                }
            }
        }, 1000, 1000);

        command = new GameCommand();
        String message = "";
        while (!clientSocket.isClosed() && !command.getCommandName().equals("FinishGame")) {
            try {
                message = in.readLine();
                System.out.println("Received: " + message);
                command = gson.fromJson(message, GameCommand.class);
                GameCommandProcess((GameCommand) command);
            } catch (IOException e) {
                System.out.println("Client disconnected");
                timer.cancel();
                return;
            }
        }
    }



    /**
     * Processing the game command in input
     * @param cmd Command to process
     */
    public void GameCommandProcess(GameCommand cmd) {

        TetrisMatch match = player.getTetrisMatch();
        switch (cmd.getCommandName()) {
            case "MOVE_LEFT" :
                match.MoveLeft();
                break;
            case "MOVE_RIGHT":
                match.MoveRight();
                break;
            case "ROTATE_CW":
                match.TryRotateClockwise();
                break;
            case "ROTATE_CCW":
                match.TryRotateCounterClockwise();
                break;
            case "SOFT_DROP":
                match.MoveDown();
                break;

            case "HARD_DROP" :
               //posso usar direttamente il ghost piece
                int ghostY = match.getTetrisBoard().GetGhostPieceY(match.getCurrentTetronimo());
                match.getCurrentTetronimo().setY(ghostY);

                match.Tick(true);
                break;



        }

        SendGameState();
    }


    private void SendGameState() {
        TetrisMatch match = player.getTetrisMatch();
        GameResponse gs = new GameResponse("TICK_UPDATE", "", player.getNickname());

        //imposto i valori aggiornati
        gs.setRemainingTime(getMyGame().getRemainingTime());
        gs.setPhase(getMyGame().getGamePhase());
        gs.setBoard(BuildBoardArray(match.getTetrisBoard()));
        gs.setCurrentShape(match.getCurrentTetronimo().getShape());
        gs.setCurrentX(match.getCurrentTetronimo().getX());
        gs.setCurrentY(match.getCurrentTetronimo().getY());
        gs.setCurrentColor(ColorToInt(match.getCurrentTetronimo().getTetronimoColor()));
        gs.setGhostY(match.getTetrisBoard().GetGhostPieceY(match.getCurrentTetronimo()));
        gs.setNextShape(match.getNextTetronimo().getShape());
        gs.setNextColor(ColorToInt(match.getNextTetronimo().getTetronimoColor()));
        gs.setScore(match.getScore());
        gs.setGameOver(player.getTetrisMatch().getTetrisBoard().IsGameOver());


        ArrayList<String> rankingInfo = new ArrayList<>();
        int pos = 1;
        for (Player p : getMyGame().getRanking()) {
            if (p == null) continue;
            rankingInfo.add(pos + ". " + p.getNickname() + " - " + p.getTetrisMatch().getScore());
            pos++;
        }
        gs.setRankingInfo(rankingInfo);

         // effetto del tetromino corrente
        gs.setCurrentHasEffect(match.getCurrentTetronimo().getHasEffect());
        if (match.getCurrentTetronimo().getHasEffect() && match.getCurrentTetronimo().getEffect() != null) {
            gs.setEffectCellRow(match.getCurrentTetronimo().getyEffect());
            gs.setEffectCellCol(match.getCurrentTetronimo().getxEffect());
            gs.setCurrentEffectName(match.getCurrentTetronimo().getEffect().getClass().getSimpleName());
        }

        // celle con effetto sulla board
        ArrayList<int[]> effectCells = new ArrayList<>();
        Cell[][] grid = match.getTetrisBoard().getGridTable();
        for (int r = 0; r < 20; r++)
            for (int c = 0; c < 10; c++)
                if (!grid[r][c].IsEmpty() && grid[r][c].getEffect() != null)
                    effectCells.add(new int[]{r, c, ColorToEffectInt(grid[r][c].getEffect())});
        gs.setEffectCells(effectCells);


        ArrayList<GameResponse.ActiveEffectInfo> effectInfos = new ArrayList<>();
        for (Effect e : match.getActiveEffects()) {
            if (e instanceof bonusDoublePoints bdp)
                effectInfos.add(new GameResponse.ActiveEffectInfo("bonusDoublePoints", bdp.getDurationTime(), 60));
            else if (e instanceof bonusSlowTimeFall bsf)
                effectInfos.add(new GameResponse.ActiveEffectInfo("bonusSlowTimeFall", bsf.getDurationTime(), 20));
            else if (e instanceof malusKalamako mk)
                effectInfos.add(new GameResponse.ActiveEffectInfo("malusKalamako", mk.getDurationTime(), 20));
            else if (e instanceof malusReversedControls mrc)
                effectInfos.add(new GameResponse.ActiveEffectInfo("malusReversedControls", mrc.getDurationTime(), 20));
        }
        gs.setActiveEffectInfos(effectInfos);

        //mando gli avversari
        Game g = getMyGame();
        ArrayList<GameResponse.OpponentBoard> opBoards = new ArrayList<>();
        for (Player p : new ArrayList<>(g.getPlayers())) {
            if (!p.getNickname().equals(player.getNickname())) {
                opBoards.add(new GameResponse.OpponentBoard(
                        p.getNickname(),
                        BuildBoardArray(p.getTetrisMatch().getTetrisBoard()),
                        p.getTetrisMatch().getScore(),
                        p.getTetrisMatch().getTetrisBoard().IsGameOver()


                ));
            }
        }
        gs.setOpponentBoards(opBoards);


        gs.setAttackerNickname(match.getLastAttacker());
        match.setLastAttacker(null);

        SendResponse(gs);
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

    public Game getMyGame() {

        for (Game ga : server.getActiveGames()) {

            if (ga.getGameID() == lobbyID){ //posso usare lo stesso lobbyID perchè han valori uguali
                return ga;
            }

        }
        return null;
    }

    public ArrayList<String> getEnemiesNicks(){

        Game g= getMyGame();

        return  g.getPlayers().stream()
                .filter(p -> !p.getNickname().equals(player.getNickname()))
                .map(Player::getNickname)
                .collect(Collectors.toCollection(ArrayList::new));


    }
    private int[][] BuildBoardArray(TetrisBoard board) {
        int[][] result = new int[20][10];
        for (int r = 0; r < 20; r++)
            for (int c = 0; c < 10; c++) {
                Cell cell = board.getGridTable()[r][c];
                result[r][c] = cell.IsEmpty() ? 0 : ColorToInt(cell.getCellColor());
            }
        return result;
    }

    private int ColorToInt(TetronimoColor color) {
        return switch (color) {
            case CYAN -> 1;
            case YELLOW -> 2;
            case ORANGE -> 3;
            case BLUE -> 4;
            case GREEN -> 5;
            case RED -> 6;
            case PURPLE -> 7;
            case GREY -> 8;
        };
    }

    private int ColorToEffectInt(Effect effect) {
        if (effect instanceof bonusDoublePoints) return 1;
        if (effect instanceof bonusRemoveARow) return 2;
        if (effect instanceof bonusBomb) return 3;
        if (effect instanceof bonusSlowTimeFall) return 4;
        if (effect instanceof malusAdd1Row) return 5;
        if (effect instanceof malusAdd2Rows) return 6;
        if (effect instanceof malusHalvePoints) return 7;
        if (effect instanceof malusKalamako) return 8;
        if (effect instanceof malusReversedControls) return 9;
        if (effect instanceof malusDoubleTetronimo) return 10;
        return 0;
    }
}