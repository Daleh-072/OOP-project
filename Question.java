package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class Question extends StackPane {
    Label question;
    Answer choice1, choice2, choice3, choice4;
    private final ImageView questionImage = new ImageView(new Image("/sample/images/buttons/questionImage.png"));

    Question(String question,
             Answer choice1 ,
             Answer choice2,
             Answer choice3,
             Answer choice4){
        this.question = new Label(question);
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.question.setFont(Font.font("Times New Roman", 20));
        this.question.setTextFill(Color.WHITE);
        this.question.setMaxWidth(350);
        this.question.setMaxHeight(80);
        this.question.setWrapText(true);
        this.question.setAlignment(Pos.CENTER);
        questionImage.setFitWidth(450);
        questionImage.setFitHeight(100);
        getChildren().addAll(questionImage, this.question);


    }
    public  ArrayList<Answer> getAnswers(){
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(choice1);
        answers.add(choice2);
        answers.add(choice3);
        answers.add(choice4);

        return answers;
    }


}
