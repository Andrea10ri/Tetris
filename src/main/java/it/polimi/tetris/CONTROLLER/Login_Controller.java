package it.polimi.tetris.CONTROLLER;


import com.google.gson.Gson;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.LoginCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class Login_Controller extends Controller{

        @FXML
        private Button btnInsertNickname;
        @FXML
        private TextField txtNickname;

        @FXML
        public void onInsertNicknameClick ()
        {
                // Send command to server
                LoginCommand cmd=new LoginCommand("NickName_Insert", this.txtNickname.getText());
                Gson gson=new Gson();
                String json=gson.toJson(cmd);
                this.virtualServer.Send(json);
        }
}
