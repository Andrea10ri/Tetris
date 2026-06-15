package it.polimi.tetris.CONTROLLER;


import com.google.gson.Gson;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.LoginCommand;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class Login_Controller extends Controller{

        @FXML
        private Button btnInsertNickname;
        @FXML
        private TextField txtNickname;
        @FXML
        private Pane logoContainer;

        //click audio
        private static final AudioClip clickSound = new AudioClip(Game_Controller.class.getResource("/it/polimi/tetris/Sounds/Click.wav").toExternalForm());




        @FXML
        public void onInsertNicknameClick ()
        {
                clickSound.play();


                // Send command to server
                LoginCommand cmd=new LoginCommand("NickName_Insert", this.txtNickname.getText());
                Gson gson=new Gson();
                String json=gson.toJson(cmd);
                this.virtualServer.Send(json);
        }

        @FXML
        private void initialize() {
                PlayLogoAnimation();
        }

        public void PlayLogoAnimation() {
                final int CELL = 40;

                Color blue = Color.web("#0324FC");

                //titolo "TETRIS" ///////////////////
                Label t1 = new Label("T");
                t1.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, 64));
                t1.setTextFill(Color.RED);

                Label e = new Label("E");
                e.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, 64));
                e.setTextFill(Color.ORANGE);

                Label t2 = new Label("T");
                t2.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, 64));
                t2.setTextFill(Color.YELLOW);

                Label r = new Label("R");
                r.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, 64));
                r.setTextFill(Color.GREEN);

                Label i = new Label("I");
                i.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, 64));
                i.setTextFill(Color.CYAN);

                Label s = new Label("S");
                s.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, 64));
                s.setTextFill(Color.PURPLE);


                HBox titleBox = new HBox(5); //4px di spazio tra lettere
                titleBox.getChildren().addAll(t1, e, t2, r, i, s);


                logoContainer.getChildren().add(titleBox);

/////////////////////

                Group one = BuildBlockGroup(
                        new int[][]{
                                {1, 1},{0, 1},{0, 1}}, Color.CYAN, CELL);

                // "0" = O-piece raddoppiato (2 colonne x 4 righe), x2
                Group zero1 = BuildBlockGroup(new int[][]{{1,1},{1,1}}, blue, CELL);
                Group zero2 = BuildBlockGroup(new int[][]{{1,1},{1,1}}, blue, CELL);

                double baseY = 90; // sotto il titolo
                one.setLayoutX(0);
                one.setLayoutY(baseY);

                double zeroOffset = CELL * 1;

                zero1.setLayoutY(baseY + zeroOffset);
                zero2.setLayoutY(baseY + zeroOffset);

// spostati a destra per evitare sovrapposizione
                zero1.setLayoutX(CELL * 2 + 16);
                zero2.setLayoutX(CELL * 2 + 16 + CELL * 2 + 16);


                logoContainer.getChildren().addAll(one, zero1, zero2);

                // posizione di partenza fuori schermo (sopra)
                one.setTranslateY(-400);
                zero1.setTranslateY(-400);
                zero2.setTranslateY(-400);
                Rectangle clip = new Rectangle();
                clip.widthProperty().bind(logoContainer.widthProperty());
                clip.heightProperty().bind(logoContainer.heightProperty());
                logoContainer.setClip(clip);

                // cadute scalate nel tempo, con piccolo rimbalzo
                AnimateDrop(one, 0);
                AnimateDrop(zero1, 200);
                AnimateDrop(zero2, 400);

                // titolo in dissolvenza dopo l'atterraggio dei pezzi

        }

        private Group BuildBlockGroup(int[][] shape, Color color, int cell) {
                Group g = new Group();
                for (int r = 0; r < shape.length; r++) {
                        for (int c = 0; c < shape[0].length; c++) {
                                if (shape[r][c] == 1) {
                                        Rectangle rect = new Rectangle(c * cell + 1, r * cell + 1, cell - 5, cell - 5);
                                        rect.setArcWidth(4);
                                        rect.setArcHeight(4);
                                        rect.setFill(color);
                                        g.getChildren().add(rect);
                                }
                        }
                }

                return g;
        }

        private void AnimateDrop(Node node, double delayMs) {
                // caduta con leggero overshoot
                TranslateTransition fall = new TranslateTransition(Duration.millis(500), node);
                fall.setDelay(Duration.millis(delayMs));
                fall.setToY(15);
                fall.setInterpolator(Interpolator.EASE_IN);

                // rimbalzo di assestamento
                TranslateTransition bounceBack = new TranslateTransition(Duration.millis(180), node);
                bounceBack.setToY(0);
                bounceBack.setInterpolator(Interpolator.EASE_OUT);

                new SequentialTransition(fall, bounceBack).play();
        }
}
