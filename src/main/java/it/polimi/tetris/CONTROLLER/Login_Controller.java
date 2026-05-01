package it.polimi.tetris.CONTROLLER;


import com.google.gson.Gson;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.LoginCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
public class Login_Controller extends Controller{

        @FXML
        private Button btnInsertNickname;
        @FXML
        private TextField txtNickname;

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
}
