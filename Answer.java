package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Answer extends StackPane {
    private Label answer;
    private boolean correct;
    private final ImageView answerImage = new ImageView(new Image("/sample/images/buttons/answerImage.png"));
    Answer(String answer){
        this(answer,false);
    }

    Answer(String answer, boolean correct){
        this.answer = new Label(answer);
        this.correct = correct;
        this.answerImage.setFitHeight(75);
        this.answerImage.setFitWidth(150);
        this.answer.setMaxHeight(65);
        this.answer.setMaxWidth(130);
        this.answer.setWrapText(true);
        this.answer.setTextAlignment(TextAlignment.CENTER);
        this.answer.setAlignment(Pos.CENTER);
        this.answer.setFont(Font.font("Times New Roman",FontWeight.BOLD, 20));
        setAlignment(Pos.CENTER);
        getChildren().addAll(answerImage,this.answer);

        this.answerImage.setOnMouseClicked(e -> showStatus());
        this.answerImage.setOnMouseReleased(e -> this.answerImage.setImage(new Image("/sample/images/buttons/answerImage.png")));
        this.answer.setOnMousePressed(e -> showStatus());
        this.answer.setOnMouseReleased(e -> this.answerImage.setImage(new Image("/sample/images/buttons/answerImage.png")));
    }
    public boolean isCorrect(){
        return this.correct;
    }
    public void showStatus(){
        if (this.correct){
            this.answerImage.setImage(new Image("sample/images/buttons/correct.png"));
        }else {
            this.answerImage.setImage(new Image("sample/images/buttons/wrong.png"));

        }
    }


}
