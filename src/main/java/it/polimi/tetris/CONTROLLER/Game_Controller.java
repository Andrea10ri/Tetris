package it.polimi.tetris.CONTROLLER;

import it.polimi.tetris.CONTROLLER.CommandsAndResponses.GameResponse;
import it.polimi.tetris.MODEL.ENUMS.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Game_Controller extends Controller{

    @FXML
    private Label lblTimer;
    @FXML
    private Label lblPhase;
    @FXML
    private Canvas mainBoard;
    @FXML
    private HBox opponentsContainer;
    @FXML
    private Label lblScore;
    @FXML
    private Label lblEffects;
    @FXML
    private Canvas nextPiece;
    @FXML
    private VBox rankingList;
    @FXML
    private HBox activeEffectsContainer;

    private int previousScore = 0;
    private static final int CELL_SIZE = 30;
    private static final int MINI_CELL = 12;
    private static final int ROWS = 20;
    private static final int COLS = 10;
    private MediaPlayer musicPlayer;
    private PlayerColor playerColor;
    private boolean isgameover = false;

    private void playMusic(String fileName) {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }

        Media media = new Media(
                Objects.requireNonNull(
                        getClass().getResource("/it/polimi/tetris/Sounds/" + fileName)
                ).toExternalForm()
        );

        musicPlayer = new MediaPlayer(media);
        musicPlayer.setVolume(0.4);
        musicPlayer.play();
    }

    private void playSound(String fileName) {
        Media media = new Media(
                Objects.requireNonNull(
                        getClass().getResource("/it/polimi/tetris/Sounds/" + fileName)
                ).toExternalForm()
        );

        MediaPlayer sfx = new MediaPlayer(media);
        sfx.setVolume(0.8);
        sfx.play();

        sfx.setOnEndOfMedia(sfx::dispose);
    }


    public void SetGame(int remainingTime, int phase,List<String> opponentNicknames, PlayerColor playerColor) {

        if (SearchLobby_Controller.lobbyBackground != null) {
            SearchLobby_Controller.lobbyBackground.stop();
            SearchLobby_Controller.lobbyBackground.dispose();
        }

        this.playerColor = playerColor; // salva il colore
        lblTimer.setText(FormatTime(remainingTime));
        lblPhase.setText("Phase " + phase);
        InitializeMainBoard(playerColor);
        SetupOpponentsBoards(opponentNicknames);
        playMusic("Tetris_OST.mp3");
    }

    //metodo per convertire da int a sessagesimale
    private String FormatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    public void SetupKeyboardInput() {

         mainBoard.getScene().setOnKeyPressed(event -> {

             if (isgameover) return;

             String cmd = null;

             switch (event.getCode()) {

                 case LEFT:
                 case A:
                     cmd = "MOVE_LEFT";
                     break;

                 case RIGHT:
                 case D:
                     cmd = "MOVE_RIGHT";
                     break;

                 case UP:
                 case W:
                     cmd = "ROTATE_CW";
                     playSound("Rotate.wav");
                     break;

                 case Z:
                 case ALT:
                     cmd = "ROTATE_CCW";
                     playSound("Rotate.wav");
                     break;

                 case DOWN:
                 case S:
                     cmd = "SOFT_DROP";
                     playSound("SoftDrop.wav");
                     break;

                 case SPACE:
                     cmd = "HARD_DROP";
                     playSound("HardDrop.wav");
                     break;

                 default:
                     cmd = null;
                     break;
             }



             if (cmd != null)
                 virtualServer.Send("{\"commandName\":\"" + cmd + "\",\"nickname\":\"" + nickname + "\"}");
         });


    }

    //metodo che inizializza la propria tetrisboard, del colore scelto
    public void InitializeMainBoard(PlayerColor playerColor) {

        GraphicsContext gc = mainBoard.getGraphicsContext2D();

        //sfondo
        gc.setFill(Color.web("#060810"));
        gc.fillRect(0, 0, mainBoard.getWidth(), mainBoard.getHeight());

        String neonColor = CuzWeTrap(playerColor);

        //effetto glow (?)
        gc.setLineWidth(1.5);
        gc.setStroke(Color.web(neonColor, 0.06));
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                gc.strokeRect(c * CELL_SIZE - 1, r * CELL_SIZE - 1, CELL_SIZE + 2, CELL_SIZE + 2);

        gc.setLineWidth(0.8);
        gc.setStroke(Color.web(neonColor, 0.12));
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                gc.strokeRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        gc.setLineWidth(0.4);
        gc.setStroke(Color.web(neonColor, 0.35));
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                gc.strokeRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        //effetto glow sul canvas stesso
        mainBoard.setEffect(new javafx.scene.effect.DropShadow(15, Color.web(neonColor, 0.9)));
    }

    //metodo di supporto a traduzione colori
    private String CuzWeTrap(PlayerColor color) {
        return switch (color) {
            case LIGHTBLUE -> "#00bfff";
            case RED -> "#ff4455";
            case PURPLE -> "#aa44ff";
            case LIGHTGREEN -> "#44ff88";
        };
    }

    //metodo che aggiorna le board nemiche
    public void SetupOpponentsBoards(List<String> opponentNicknames) {

        if (!opponentsContainer.getChildren().isEmpty()) return;
        opponentsContainer.getChildren().clear();


        VBox vContainer = new VBox(10);
        vContainer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(vContainer, Priority.ALWAYS);

        if (opponentNicknames.size() <= 2) {


            //adattamento a schermata
            //potrei fare che se i nemiici sono 2 allora son di fianco se 3 allora son 2 su uno giu
            HBox row = new HBox(10);
            for (String nick : opponentNicknames) {
                VBox panel = CreateOpponentPanel(nick);
                HBox.setHgrow(panel, Priority.ALWAYS);
                row.getChildren().add(panel);
            }
            vContainer.getChildren().add(row);
        }

        else {

            HBox topRow = new HBox(10);
            VBox panel1 = CreateOpponentPanel(opponentNicknames.get(0));
            VBox panel2 = CreateOpponentPanel(opponentNicknames.get(1));
            HBox.setHgrow(panel1, Priority.ALWAYS);
            HBox.setHgrow(panel2, Priority.ALWAYS);
            topRow.getChildren().addAll(panel1, panel2);

            HBox bottomRow = new HBox(10);
            bottomRow.setAlignment(javafx.geometry.Pos.CENTER);
            VBox panel3 = CreateOpponentPanel(opponentNicknames.get(2));
            panel3.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(panel3, Priority.ALWAYS);
            bottomRow.getChildren().add(panel3);

            vContainer.getChildren().addAll(topRow, bottomRow);
        }

        opponentsContainer.getChildren().add(vContainer);
    }

    private VBox CreateOpponentPanel(String nick) {
        VBox panel = new VBox(6);
        panel.setStyle("-fx-border-color: rgba(255,255,255,0.1); -fx-border-width: 1; -fx-padding: 8; -fx-alignment: CENTER; -fx-background-color: #060810;");
        panel.setMaxWidth(Double.MAX_VALUE);
        panel.setPrefHeight(USE_COMPUTED_SIZE);
        panel.setMaxHeight(USE_COMPUTED_SIZE);

        Label lblNick = new Label(nick);
        lblNick.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: white;");

        Canvas miniBoard = new Canvas(COLS * MINI_CELL, ROWS * MINI_CELL);
        miniBoard.setId("board_" + nick);
        InitializeMiniBoard(miniBoard.getGraphicsContext2D());

        Label lblOpScore = new Label("0");
        lblOpScore.setId("score_" + nick);
        lblOpScore.setStyle("-fx-font-size: 10px; -fx-text-fill: white;");

        panel.getChildren().addAll(lblNick, miniBoard, lblOpScore);
        return panel;
    }

    //metodo che aggiorna la propria board
    public void RenderGameState(GameResponse response) {
        GraphicsContext gc = mainBoard.getGraphicsContext2D();

        lblTimer.setText(FormatTime(response.getRemainingTime()));
        lblPhase.setText("PHASE " + response.getPhase());

        //ridisegna sfondo e griglia
        InitializeMainBoard(playerColor);

        //disegna le celle occupate
        if (response.getBoard() != null)
            for (int r = 0; r < ROWS; r++){
                for (int c = 0; c < COLS; c++){
                    if (response.getBoard()[r][c] != 0){

                        DrawCell(gc, r, c, IntToColor(response.getBoard()[r][c]), CELL_SIZE);}
                }
            }

        //ghost piece
        if (response.getCurrentShape() != null) {
            gc.setGlobalAlpha(0.25);
            for (int r = 0; r < response.getCurrentShape().length; r++){
                for (int c = 0; c < response.getCurrentShape()[0].length; c++){
                    if (response.getCurrentShape()[r][c] == 1){
                        DrawCell(gc, response.getGhostY() + r, response.getCurrentX() + c, Color.WHITE, CELL_SIZE);
                    }
                }
            }
            gc.setGlobalAlpha(1.0);
        }

        //tetromino corrente
        if (response.getCurrentShape() != null) {
            Color currentColor = IntToColor(response.getCurrentColor());
            for (int r = 0; r < response.getCurrentShape().length; r++){
                for (int c = 0; c < response.getCurrentShape()[0].length; c++){
                    if (response.getCurrentShape()[r][c] == 1){
                        DrawCell(gc, response.getCurrentY() + r, response.getCurrentX() + c, currentColor, CELL_SIZE);}
                }
            }
        }

        //disegna la cella con effetto
        if (response.isCurrentHasEffect() && response.getCurrentShape() != null) {
            int effectRow = response.getEffectCellRow();
            int effectCol = response.getEffectCellCol();

            // verifica che la cella sia occupata nella shape e nei bounds
            if (effectRow >= 0 && effectRow < response.getCurrentShape().length &&
                    effectCol >= 0 && effectCol < response.getCurrentShape()[0].length &&
                    response.getCurrentShape()[effectRow][effectCol] == 1) {

                int boardRow = response.getCurrentY() + effectRow;
                int boardCol = response.getCurrentX() + effectCol;
                DrawEffectCell(gc, boardRow, boardCol, CELL_SIZE, response.getCurrentEffectName());
            }
        }

        //disegna icone sulle celle della board con effetto
        if (response.getEffectCells() != null)
            for (int[] cell : response.getEffectCells())
                DrawEffectCell(gc, cell[0], cell[1], CELL_SIZE, IntToEffectName(cell[2]));

        RenderActiveEffects(response.getActiveEffectInfos());

        //tetronimo successivo
        RenderNextPiece(response.getNextShape(), response.getNextColor());

        //score
        if (lblScore != null)
            lblScore.setText(String.valueOf(response.getScore()));

        UpdateRanking(response.getRankingInfo());

        if (response.getOpponentBoards() != null)
            for (GameResponse.OpponentBoard op : response.getOpponentBoards()){
                UpdateOpponentBoard(op.getNickname(), op.getBoard(), op.getScore(), op.isGameOver());
            }



        //oscuramento kalamako
        if (hasKalamako(response)) {

            //overlay scuro
            gc.setFill(Color.web("#000000", 0.45));
            gc.fillRect(0, 0, mainBoard.getWidth(), mainBoard.getHeight());

            //immagine macchia
            Image ink = new Image(getClass().getResourceAsStream("/it/polimi/tetris/effects/kalamako.png"));

            //gc.setGlobalAlpha(0.85); //trasparenza immagine
            gc.drawImage(ink, 0, 0, mainBoard.getWidth(), mainBoard.getHeight());
            gc.setGlobalAlpha(1.0); //reset
        }

        //audio elimination row
        int newScore = response.getScore();
        if (newScore > previousScore) {
            playSound("Cleared.wav");
        }
        previousScore = newScore;

        //game over
        if (response.isGameOver()) {
            RenderGameOverOverlay(gc);
            isgameover= true;
         //   musicPlayer.stop();
        }


    }

    private boolean hasKalamako(GameResponse response) {
        if (response.getActiveEffectInfos() == null) return false;

        for (GameResponse.ActiveEffectInfo info : response.getActiveEffectInfos()) {
            if ("malusKalamako".equals(info.getEffectName()))
                return true;
        }
        return false;
    }

    private void UpdateRanking(ArrayList<String> rankingInfo) {
        if (rankingInfo == null || rankingList == null) return;

        rankingList.getChildren().clear();
        for (String entry : rankingInfo) {
            Label lbl = new Label(entry);
            lbl.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
            rankingList.getChildren().add(lbl);
        }
    }

    //metodo che gestisce dinamicamente le immagini e il timer degli effetti attivati
    private void RenderActiveEffects(ArrayList<GameResponse.ActiveEffectInfo> effects) {

        activeEffectsContainer.getChildren().clear();
        if (effects == null) return;

        for (GameResponse.ActiveEffectInfo info : effects) {

            StackPane effectPane = new StackPane();
            effectPane.setPrefSize(50, 50);

            // icona effetto
            Image icon = GetEffectIcon(info.getEffectName());
            if (icon != null) {
                javafx.scene.image.ImageView iv = new javafx.scene.image.ImageView(icon);
                iv.setFitWidth(50);
                iv.setFitHeight(50);
                effectPane.getChildren().add(iv);
            }

            // progressbar sovrapposta in basso
            double progress = (double) info.getRemainingTicks() / info.getTotalTicks();
            javafx.scene.shape.Rectangle bgBar = new javafx.scene.shape.Rectangle(50, 6);
            bgBar.setFill(Color.web("#333333"));
            bgBar.setTranslateY(22);

            javafx.scene.shape.Rectangle fgBar = new javafx.scene.shape.Rectangle(50 * progress, 6);
            fgBar.setFill(Color.web("#00ff88"));
            fgBar.setTranslateY(22);
            fgBar.setTranslateX(-(50 - 50 * progress) / 2);

            effectPane.getChildren().addAll(bgBar, fgBar);
            activeEffectsContainer.getChildren().add(effectPane);
        }
    }

    private void DrawEffectCell(GraphicsContext gc, int row, int col, int cellSize, String effectName) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return;

        // icona effetto
        Image icon = GetEffectIcon(effectName);

            gc.drawImage(icon, col * cellSize + 3, row * cellSize + 3,
                    cellSize - 6, cellSize - 6);

    }

    private Image GetEffectIcon(String effectName) {
        if (effectName == null) return null;
        try {
            return new Image(getClass().getResourceAsStream(
                    "/it/polimi/tetris/effects/" + effectName + ".png"));
        }
        catch (Exception e) {
            return null;
        }
    }

    private void UpdateOpponentBoard(String nickname, int[][] board, int score, boolean gameOver) {
        Canvas miniBoard = (Canvas) opponentsContainer.lookup("#board_" + nickname);
        Label scoreLabel = (Label) opponentsContainer.lookup("#score_" + nickname);

        if (miniBoard != null) {
            GraphicsContext gc = miniBoard.getGraphicsContext2D();
            InitializeMiniBoard(gc);
            if (board != null)
                for (int r = 0; r < ROWS; r++)
                    for (int c = 0; c < COLS; c++)
                        if (board[r][c] != 0)
                            DrawCell(gc, r, c, IntToColor(board[r][c]), MINI_CELL);

            //overlay game over sulla mini board
            if (gameOver) {
                gc.setFill(Color.web("#000000", 0.65));
                gc.fillRect(0, 0, miniBoard.getWidth(), miniBoard.getHeight());
                gc.setFill(Color.web("#ff4455"));
                gc.setFont(javafx.scene.text.Font.font("Courier New", javafx.scene.text.FontWeight.BOLD, 8));
                gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
                gc.fillText("GAME OVER", miniBoard.getWidth() / 2, miniBoard.getHeight() / 2);
            }
        }

        if (scoreLabel != null)
            scoreLabel.setText(String.valueOf(score));
    }

    private void RenderGameOverOverlay(GraphicsContext gc) {

        //audio
        playMusic("GameOver.wav");

        //overlay scuro semitrasparente
        gc.setFill(Color.web("#000000", 0.65));
        gc.fillRect(0, 0, mainBoard.getWidth(), mainBoard.getHeight());


        gc.setFill(Color.web("#ff4455"));
        gc.setFont(javafx.scene.text.Font.font("Courier New", javafx.scene.text.FontWeight.BOLD, 28));
        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        gc.fillText("GAME OVER", mainBoard.getWidth() / 2, mainBoard.getHeight() / 2 - 10);

        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Courier New", 16));
        gc.fillText("Score: " + lblScore.getText(), mainBoard.getWidth() / 2, mainBoard.getHeight() / 2 + 20);
    }

    private void RenderNextPiece(int[][] shape, int colorCode) {
        GraphicsContext gc = nextPiece.getGraphicsContext2D();
        gc.setFill(Color.web("#060810"));
        gc.fillRect(0, 0, nextPiece.getWidth(), nextPiece.getHeight());

        if (shape == null) return;

        Color color = IntToColor(colorCode);
        int cellSize = 18;
        int offsetX = (int)(nextPiece.getWidth() - shape[0].length * cellSize) / 2;
        int offsetY = (int)(nextPiece.getHeight() - shape.length * cellSize) / 2;

        for (int r = 0; r < shape.length; r++)
            for (int c = 0; c < shape[0].length; c++)
                if (shape[r][c] == 1) {
                    gc.setFill(color);
                    gc.fillRoundRect(offsetX + c * cellSize + 1, offsetY + r * cellSize + 1,
                            cellSize - 2, cellSize - 2, 3, 3);
                }
    }

    private void DrawCell(GraphicsContext gc, int row, int col, Color color, int cellSize) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return;
        gc.setFill(color);
        gc.fillRoundRect(col * cellSize + 1, row * cellSize + 1, cellSize - 2, cellSize - 2, 3, 3);
        gc.setStroke(color.brighter());
        gc.setLineWidth(1);
        gc.strokeRoundRect(col * cellSize + 1, row * cellSize + 1, cellSize - 2, cellSize - 2, 3, 3);
    }

    private Color IntToColor(int code) {
        return switch (code) {
            case 1 -> Color.CYAN;
            case 2 -> Color.BLUE;
            case 3 -> Color.ORANGE;
            case 4 -> Color.YELLOW;
            case 5 -> Color.GREEN;
            case 6 -> Color.PURPLE;
            case 7 -> Color.RED;
            case 8 -> Color.GRAY;
            default -> Color.WHITE;
        };
    }

    private String IntToEffectName(int code) {
        return switch (code) {
            case 1 -> "bonusDoublePoints";
            case 2 -> "bonusRemoveARow";
            case 3 -> "bonusBomb";
            case 4 -> "bonusSlowTimeFall";
            case 5 -> "malusAdd1Row";
            case 6 -> "malusAdd2Rows";
            case 7 -> "malusHalvePoints";
            case 8 -> "malusKalamako";
            case 9 -> "malusReversedControls";
            case 10 -> "malusDoubleTetronimo";
            default -> null;
        };
    }

    private void InitializeMiniBoard(GraphicsContext gc) {
        gc.setFill(Color.web("#060810"));
        gc.fillRect(0, 0, COLS * MINI_CELL, ROWS * MINI_CELL);
        gc.setStroke(Color.web("#ffffff", 0.08));
        gc.setLineWidth(0.3);
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                gc.strokeRect(c * MINI_CELL, r * MINI_CELL, MINI_CELL, MINI_CELL);
    }

    //metodo che prende dal ranking info nome e colore e mostra la classifica finale
    public void ShowWinner(ArrayList<String> rankingInfo) {

        //audio
        playMusic("Victory.wav");

        isgameover= true;

        GraphicsContext gc = mainBoard.getGraphicsContext2D();

        //overlay scuro
        gc.setFill(Color.web("#000000", 0.80));
        gc.fillRect(0, 0, mainBoard.getWidth(), mainBoard.getHeight());


        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        gc.setFill(Color.web("#ffcc00"));
        gc.fillText("YOU'RE THE WINNER!", mainBoard.getWidth() / 2, 80);

        //ranking finale
        if (rankingInfo != null) {
            double y = 130;
            for (String entry : rankingInfo) {
                String[] parts = entry.split("\\|");
                if (parts.length < 3) continue;
                String text = parts[0] + parts[1];
                String colorHex = parts[2];
                gc.setFill(Color.web(colorHex));
                gc.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
                gc.fillText(text, mainBoard.getWidth() / 2, y);
                y += 35;
            }
        }
    }

    public Label getLblTimer() {
        return lblTimer;
    }
    public void setLblTimer(Label lblTimer) {
        this.lblTimer = lblTimer;
    }
    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }
}
