package GUI;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import system.ShellCommands;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller{
    public static TextArea ta;

    @FXML
    TextField textField;
    @FXML
    TextArea textArea=new TextArea();

     static String input;
     ShellCommands sh=new ShellCommands();

    @FXML
    public void initialize() {
        ta=textArea;
        textField.setOnAction(e -> {
            String text = sh.getCommand(textField.getText());
            textArea.appendText(">>"+textField.getText()+"\n");
            textField.clear();
            if(text==null || text.equals(""))
            {
                textArea.appendText("no data\n");
            }
            else ta.appendText(text + "\n");

        });
    }

    public static void updateTa(String s)
    {
        ta.appendText(s+"\n");
    }
}
