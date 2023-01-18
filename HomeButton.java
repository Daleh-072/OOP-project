package sample;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_INPeer;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class HomeButton extends ImageView {
    private Image defualt, pressed, selected;
    private String buttonType;


    public HomeButton(
            String buttonType,
            int width,
            int higth,
            Scene scene
    ){
        this.defualt = new Image("sample/images/buttons/un"+buttonType+".png");
        this.pressed = new Image("sample/images/buttons/pr"+buttonType+".png");
        this.buttonType = buttonType;
        setImage(defualt);
        setFitWidth(width);
        setFitHeight(higth);

        setOnMousePressed(e -> {
            this.setPressed();

        });
        setOnMouseReleased(e -> {
            this.setDefualt();

        });
        setOnMouseEntered(e-> scene.setCursor(Cursor.HAND));
        setOnMouseExited(e-> scene.setCursor(Cursor.DEFAULT));





    }

    public HomeButton(
            String buttonType,
            Scene scene
    ){
        this(buttonType, 150,75, scene);
    }
    public void setDefualt(){
        setImage(defualt);
    }
    public void setSelected(){
        setImage(selected);
    }
    public void setPressed(){
        setImage(pressed);
    }


}
