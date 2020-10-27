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

public class AnonymousPalette extends Application {
	BorderPane root = new BorderPane();
	Rectangle color = new Rectangle (200, 100);
	@Override
	public void start(Stage primaryStage) throws Exception {
		setScreen();
		Scene scene = new Scene(root, 200, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("My Color Palette");
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
	private void setScreen() {
		color.setFill(Color.WHITE);
		Button blue = new Button ("Blue");
		Button red = new Button("Red");
		blue.setPrefSize(100,  200);
		red.setPrefSize(100, 100);

		blue.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent event) {
					color.setFill(Color.BLUE);
			} //end handle()
		}) ; //end new EventHandler()

		red.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent event) {
				color.setFill(Color.RED);
			} //end handle()
		}) ; //end new EventHandler()
		root.setTop(color);
		root.setLeft(blue);
		root.setRight(red);
	}
}

