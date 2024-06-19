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

    @FXML
    TextField textField=new TextField();
    @FXML
    TextArea textArea=new TextArea();

    private static String input;

    @FXML
    public void initialize() {
        textField.setOnAction(e -> {
            String text = ShellCommands.getCommand(textField.getText());
            textArea.appendText(">>"+textField.getText()+"\n");
            textField.clear();
            if(text==null || text.equals(""))
            {
                textArea.appendText("no data\n");
            }
            else textArea.appendText(text + "\n");

        });
    }


    public TextArea getTextArea() {
        return textArea;
    }

}
