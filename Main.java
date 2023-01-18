package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.*;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private static ArrayList<Question> questionsList = new ArrayList<>();
	private Timeline game = new Timeline();
	private Timeline deadEffect = new Timeline();
	private Group recGroup = new Group();
	private AnimationTimer gameFrames;
	private int questionNumber = 0;
	private Pane monstartPane = new Pane();
	private ImageView moster = new ImageView();
	private long speed = 5000;
	private ImageView deathEffect = new ImageView(new Image("sample/images/mons/mosdead.gif"));
	private ImageView pren = new ImageView(new Image("sample/images/mons/pren.gif"));
	private int randomNumber, randomPic;
	private ImageView backgroundD = new ImageView(new Image("/sample/images/mons/dunBk.png"));
	private long startTime, endTime;
	private File startVidoe = new File("Untitled1.mp4");
	private Media startM = new Media(startVidoe.toURI().toString());
	private MediaPlayer startMedia = new MediaPlayer(startM);
	private MediaView startView = new MediaView(startMedia);
	private boolean firstTime = true;
	private File backgroungSound = new File("bk.wav");
	private Media backSoundMedia = new Media(backgroungSound.toURI().toString());
	private MediaPlayer gameSound = new MediaPlayer(backSoundMedia);
	private MediaPlayer killSound = new MediaPlayer(new Media((new File("killSound.wav").toURI().toString())));
	private MediaPlayer wrongSound = new MediaPlayer(new Media((new File("wrong.wav").toURI().toString())));

	boolean flage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Reading the quistions from the dat file
		ImageView skipButton = new ImageView(new Image("sample/images/buttons/skip.png"));
		skipButton.setFitWidth(70);
		skipButton.setFitHeight(50);
		skipButton.setTranslateY(450);
		skipButton.setTranslateX(400);
		skipButton.setOpacity(0.4);
		Pane root = new Pane();
		root.setPrefHeight(500);
		root.setPrefWidth(500);
		startView.setFitWidth(500);
		startView.setFitHeight(500);
		startView.setTranslateX(0);
		startView.setTranslateY(0);

		startMedia.play();
		root.getChildren().add(startView);
		root.getChildren().add(skipButton);
		Scene firstAnimationScene = new Scene(root, 500, 500);
		primaryStage.setScene(firstAnimationScene);

		firstAnimationScene.setFill(Color.BLACK);
		Timeline tl = new Timeline();
		tl.getKeyFrames().add(new KeyFrame(Duration.seconds(50), e -> {
			mainScene(primaryStage);
			tl.stop();
			startMedia.stop();

		}));
		skipButton.setOnMouseClicked(e -> {
			mainScene(primaryStage);
			startMedia.stop();
			tl.stop();

		});
		skipButton.setOnMouseEntered(e -> {
			firstAnimationScene.setCursor(Cursor.HAND);
			skipButton.setOpacity(1);
		});
		skipButton.setOnMouseExited(e -> {

			firstAnimationScene.setCursor(Cursor.DEFAULT);
			skipButton.setOpacity(0.4);
		});
		tl.play();
		primaryStage.setScene(firstAnimationScene);

		try (Scanner file = new Scanner(
				new File("C:\\Users\\daleh\\Downloads\\ICS108Project\\ICS108Project\\src\\sample\\Questions.txt"))) {
			String tempLine;
			String[] tempArray = new String[5];
			while (file.hasNext()) {
				tempLine = file.nextLine();
				tempArray = tempLine.split(",");
				System.out.println(Arrays.toString(tempArray));
				questionsList.add(new Question(tempArray[0], new Answer(tempArray[1], true), new Answer(tempArray[2]),
						new Answer(tempArray[3]), new Answer(tempArray[4])));

			}
		}

		primaryStage.show();
	}

	public static void main(String[] args) throws IOException {
		launch(args);

	}

	public void startGame(Stage primaryStage) {
		monstartPane = new Pane();
		flage = false;
		BorderPane gamePane = new BorderPane();
		VBox question = new VBox(10);
		Label test = new Label("ABOUT BLA BLA BLA");
		test.setFont(Font.font("Times New Roman", 30));
		GridPane anserPane = new GridPane();
		startTime = System.currentTimeMillis();
		question.getChildren().add(anserPane);
		question.setAlignment(Pos.CENTER);
		anserPane.setAlignment(Pos.CENTER);

		anserPane.setVgap(10);

		Scene gameStartScene = new Scene(gamePane, 500, 500);
		// testing
		pren.setFitHeight(40);
		pren.setFitWidth(30);
		pren.setTranslateX(225);
		pren.setTranslateY(100);
		backgroundD.setFitWidth(500);
		backgroundD.setFitHeight(150);
		moster.setFitHeight(75);
		moster.setFitWidth(75);
		moster.setTranslateX(-75);
		deathEffect.setOpacity(0);
		deathEffect.setFitWidth(75);
		deathEffect.setFitHeight(75);
		moster.setTranslateY(75);
		deathEffect.setTranslateY(75);
		deathEffect.setTranslateX(-100);
		monstartPane.getChildren().addAll(backgroundD, deathEffect, pren, moster);

//        monstartPane.getChildren().add(deathEffect);
		gamePane.setBottom(monstartPane);

		gamePane.setCenter(getQuestion(gamePane, true));

		primaryStage.setScene(gameStartScene);
		Label score = new Label("0");
		score.setTextFill(Color.BLACK);
		score.setFont(Font.font("Verdana", 25));
		score.setAlignment(Pos.CENTER);
		score.setTextAlignment(TextAlignment.CENTER);
		score.setTranslateX(210);
		gamePane.setTop(score);
		gameFrames = new AnimationTimer() {
			@Override
			public void handle(long l) {
				if ((moster.getTranslateX() >= 155 && randomNumber == 1)
						|| (moster.getTranslateX() <= 250 && randomNumber == 0)) {
					endTime = System.currentTimeMillis();
					Timeline endGame = new Timeline();
					endGame.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5), e -> gameOverStage(primaryStage)));
//                    speed=5000;
					endGame.play();
					gameSound.stop();
					gameFrames.stop();
				}
				score.setText("" + ((System.currentTimeMillis() - startTime) / 1000.0));
			}
		};
		gameFrames.start();

	}

	public void aobutScene(Stage primaryStage) {

	}

	public void addScene(Stage primaryStage) {

		TextField textF = new TextField();

		textF.setPromptText(" QUESTION");
		textF.setPadding(new Insets(0));
		textF.setFont(new Font(50));
		textF.setAlignment(Pos.CENTER);

		Text questionNum = new Text("QUESTION NUMBER #");
		questionNum.setFont(new Font(25));

		TextField A1 = new TextField();
		A1.setFont(new Font(20));
		A1.setPromptText(" CORRECT ANSWER");

		TextField A2 = new TextField();
		A2.setFont(new Font(20));

		A2.setPromptText(" WRONG ANSWER");

		TextField A3 = new TextField();
		A3.setFont(new Font(20));

		A3.setPromptText(" WRONG ANSWER");

		TextField A4 = new TextField();
		A4.setFont(new Font(20));

		A4.setPromptText(" WRONG ANSWER");

		HBox vb = new HBox(A1);
		HBox vb2 = new HBox(A2);
		HBox vb3 = new HBox(A3);
		HBox vb4 = new HBox(A4);

		TilePane tile = new TilePane(vb, vb2);

		TilePane tile2 = new TilePane(vb3, vb4);

		tile.setHgap(100);
		tile.setVgap(100);
		tile2.setHgap(100);
		tile2.setVgap(100);

		tile.setAlignment(Pos.CENTER);
		tile2.setAlignment(Pos.CENTER);

		Button submit = new Button("Submit");
		submit.setFont(new Font(20));

		BorderPane del = new BorderPane();
		HBox hbox = new HBox(submit);
		hbox.setSpacing(30);
		hbox.setPadding(new Insets(150));
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		del.setBottom(hbox);

		HBox hbb = new HBox(textF);
		hbb.setPrefSize(50, 50);
		hbb.setPadding(new Insets(10));
		hbb.setAlignment(Pos.CENTER);
		VBox F = new VBox(hbb, tile, tile2);
		F.setAlignment(Pos.CENTER);
		F.setSpacing(40);

		VBox FF = new VBox(questionNum);
		FF.setAlignment(Pos.CENTER);
		FF.getChildren().add(F);
		FF.getChildren().add(del);
		hbox.setAlignment(Pos.CENTER);
		BorderPane BP = new BorderPane(FF);
		BorderPane PB2 = new BorderPane();
		Scene scene = new Scene(BP, 800, 700);
		HomeButton back = new HomeButton("Back", 100, 50, scene);
		PB2.setTop(back);
		PB2.setTranslateX(20);
		PB2.setTranslateY(20);

		BP.setTop(PB2);

		primaryStage.setScene(scene);

		primaryStage.show();
		back.setOnMouseClicked(e -> mainScene(primaryStage));

	}

	public void delScene(Stage primaryStage) {
		ComboBox<String> cbquestinos = new ComboBox();
		Label text = new Label("Questions");
		text.setFont(new Font(50));
		String[] questions = new String[10];

		for (int i = 0; i < questions.length; i++) {
			questions[i] = "Question" + (i + 1);
		}

		String[] answers = new String[10];

		for (int i = 0; i < questions.length; i++) {
			answers[i] = "Q" + (i + 1) + "_A";
		}

		Label LabelQuestions = new Label("", cbquestinos);
		LabelQuestions.setAlignment(Pos.TOP_RIGHT);
		LabelQuestions.setContentDisplay(ContentDisplay.RIGHT);

		TextField textF = new TextField();

		textF.setPromptText(" QUESTION");
		textF.setPadding(new Insets(0));
		textF.setFont(new Font(50));
		textF.setAlignment(Pos.CENTER);

		Text questionNum = new Text("QUESTION NUMBER #");
		questionNum.setFont(new Font(25));

		TextField A1 = new TextField();
		A1.setFont(new Font(20));
		A1.setPromptText(" CORRECT ANSWER");

		TextField A2 = new TextField();
		A2.setFont(new Font(20));

		A2.setPromptText(" WRONG ANSWER");

		TextField A3 = new TextField();
		A3.setFont(new Font(20));

		A3.setPromptText(" WRONG ANSWER");

		TextField A4 = new TextField();
		A4.setFont(new Font(20));

		A4.setPromptText(" WRONG ANSWER");

		cbquestinos.getItems().addAll(questions);
		cbquestinos.setValue("Questions");
		cbquestinos.setOnAction(e -> {
			questionNum.setText(cbquestinos.getValue());
			textF.setText(cbquestinos.getValue());
			A1.setText(cbquestinos.getValue() + "_A1");
			A2.setText(cbquestinos.getValue() + "_A2");
			A3.setText(cbquestinos.getValue() + "_A3");
			A4.setText(cbquestinos.getValue() + "_A4");

		});

		HBox vb = new HBox(A1);
		HBox vb2 = new HBox(A2);
		HBox vb3 = new HBox(A3);
		HBox vb4 = new HBox(A4);

		TilePane tile = new TilePane(vb, vb2);

		TilePane tile2 = new TilePane(vb3, vb4);

		tile.setHgap(100);
		tile.setVgap(100);
		tile2.setHgap(100);
		tile2.setVgap(100);

		tile.setAlignment(Pos.CENTER);
		tile2.setAlignment(Pos.CENTER);

		Button delete = new Button("Delete");
		delete.setFont(new Font(20));

		Button submit = new Button("Submit");
		submit.setFont(new Font(20));

		BorderPane del = new BorderPane();
		HBox hbox = new HBox(delete, submit);
		hbox.setSpacing(30);
		hbox.setPadding(new Insets(150));
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		del.setBottom(hbox);

		HBox hbb = new HBox(textF);
		hbb.setPrefSize(50, 50);
		hbb.setPadding(new Insets(10));
		hbb.setAlignment(Pos.CENTER);
		VBox F = new VBox(hbb, tile, tile2);
		F.setAlignment(Pos.CENTER);
		F.setSpacing(40);

		VBox FF = new VBox(questionNum);
		FF.setAlignment(Pos.CENTER);
		FF.getChildren().add(text);
		FF.getChildren().add(F);
		FF.getChildren().add(del);
		hbox.setAlignment(Pos.CENTER);
		BorderPane BP = new BorderPane(FF);
		BorderPane PB2 = new BorderPane();
		Scene scene = new Scene(BP, 800, 700);
		HomeButton back = new HomeButton("Back", 100, 50, scene);
		PB2.setTop(back);
		PB2.setTranslateX(20);
		PB2.setTranslateY(20);

		BP.setTop(PB2);
		BP.setRight(LabelQuestions);

		primaryStage.setScene(scene);

		primaryStage.show();
		back.setOnMouseClicked(e -> mainScene(primaryStage));

	}

	public VBox getQuestion(BorderPane pane, boolean correct) {

		wrongSound.stop();

		if (correct) {
			killSound.stop();
			double monsPos = moster.getTranslateX();
			game.stop();

			randomNumber = (int) (Math.random() * 2);
			randomPic = (int) (Math.random() * 3);
			System.out.println(randomPic + "" + randomNumber);
			moster.setTranslateX(randomNumber == 0 ? 575 : -80);
			moster.setImage(new Image("/sample/images/mons/mons" + randomPic + "" + randomNumber + ".gif"));
//            speed-=500;
			game.getKeyFrames()
					.add(new KeyFrame(Duration.millis(speed), new KeyValue(moster.translateXProperty(), 210)));
			game.play();
			if (flage) {
				killSound.play();
			}
			flage = true;
			showDeathEffect(monsPos);

		} else if (!correct) {
			wrongSound.play();
		}

		if (questionNumber >= questionsList.size()) {
			questionNumber = 0;
		}
		VBox newQuestion = new VBox(10);
		newQuestion.getChildren().add(questionsList.get(questionNumber));
		GridPane answers = new GridPane();
		answers.setVgap(10);
		ArrayList<Answer> tempA = questionsList.get(questionNumber).getAnswers();
		for (Answer i : tempA) {
			i.setOnMouseClicked(e -> {

				pane.setCenter(getQuestion(pane, ((Answer) e.getSource()).isCorrect()));
			});
		}

		Collections.shuffle(tempA);
		answers.add(tempA.get(0), 0, 0);
		answers.add(tempA.get(1), 0, 1);
		answers.add(tempA.get(2), 1, 0);
		answers.add(tempA.get(3), 1, 1);
		answers.setAlignment(Pos.CENTER);

		newQuestion.getChildren().add(answers);
		newQuestion.setAlignment(Pos.CENTER);
		questionNumber++;
		return newQuestion;
	}

	public void gameOverStage(Stage primaryStage) {
		VBox gamePane = new VBox(30);
		long Score = endTime - startTime;
		gamePane.setAlignment(Pos.CENTER);
//        gamePane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		Label test = new Label("The princess has died!");
		test.setTextFill(Color.RED);
		Label scoreLabel = new Label("she survived " + (Score / 1000.0) + " seconds");
		scoreLabel.setTextFill(Color.BLACK);
		scoreLabel.setFont(Font.font("Times New Roman", 30));
		test.setFont(Font.font("Times New Roman", 30));
		gamePane.getChildren().add(test);
		gamePane.getChildren().add(scoreLabel);
		Scene delScene = new Scene(gamePane, 500, 500);
		primaryStage.setScene(delScene);
		HomeButton res = new HomeButton("Res", delScene);
		HomeButton home = new HomeButton("Home", delScene);

		res.setOnMouseClicked(e -> startGame(primaryStage));
		home.setOnMouseClicked(e -> mainScene(primaryStage));
		gamePane.getChildren().add(res);
		gamePane.getChildren().add(home);

	}

	public void showDeathEffect(double x) {
		System.out.println("Im in with x = " + x);
		deathEffect.setImage(new Image("sample/images/mons/mosdead.gif"));
		deathEffect.setTranslateX(x);
		deathEffect.setOpacity(1);
		deadEffect.getKeyFrames().add(new KeyFrame(Duration.millis(500), e -> deathEffect.setOpacity(0)));
		deadEffect.play();
	}

	public void f(Stage primaryStage) {

	}

	public void mainScene(Stage primaryStage) {
		ImageView title = new ImageView(new Image("sample/images/title.png"));
		title.setFitHeight(150);
		title.setFitWidth(300);
		title.setTranslateX(20);
		Collections.shuffle(questionsList);

		// Here we define the pane to put them in the scenes.
		VBox mainPane = new VBox(10); // We will use VBox for the main menu.
		mainPane.setPadding(new Insets(50, 50, 50, 50));
		mainPane.setAlignment(Pos.CENTER);
		mainPane.getChildren().add(title);
		/*
		 * Here we define the scene that we are going to build during this project.
		 */
		Scene mainScene = new Scene(mainPane, 500, 500);
		HomeButton playButton = new HomeButton("Play", mainScene);
		HomeButton addButton = new HomeButton("Add", 75, 75, mainScene);
		HomeButton delButton = new HomeButton("Del", 75, 75, mainScene);
		HomeButton aboutButton = new HomeButton("About", mainScene);
		HBox modButtons = new HBox();
		modButtons.setAlignment(Pos.CENTER);
		modButtons.getChildren().addAll(addButton, delButton);

		mainPane.getChildren().addAll(playButton, modButtons, aboutButton);

		/*
		 * here we are adding the functionality of the buttons to change the scene.
		 */
		playButton.setOnMouseClicked(e -> {
			startGame(primaryStage);
			gameSound.play();

		});
		delButton.setOnMouseClicked(e -> delScene(primaryStage));
		addButton.setOnMouseClicked(e -> addScene(primaryStage));
		aboutButton.setOnMouseClicked(e -> aobutScene(primaryStage));

		primaryStage.setScene(mainScene);

	}
}
