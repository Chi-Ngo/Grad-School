package week8;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ThisPalette extends Application implements EventHandler<ActionEvent>{
	BorderPane root = new BorderPane();
	Rectangle color = new Rectangle (200, 100);
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		setScreen();
		Scene scene = new Scene(root, 200, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("My Color Palette");
		primaryStage.show();
	}
	private void setScreen() {
		color.setFill(Color.WHITE);
		Button blue = new Button ("Blue");
		Button red = new Button("Red");
		blue.setPrefSize(100,  200);
		red.setPrefSize(100, 100);
		blue.setOnAction(this); //’this’ itself is a handler
		red.setOnAction(this);
		root.setTop(color);
		root.setLeft(blue);
		root.setRight(red);
	}
	@Override
	public void handle(ActionEvent event) {
		Button b = (Button)event.getSource();
		switch (b.getText()) {
			case "Blue": color.setFill(Color.BLUE); break;
			case "Red": color.setFill(Color.RED); break;
		}
	}
}


