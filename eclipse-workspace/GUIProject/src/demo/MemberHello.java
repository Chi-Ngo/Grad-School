package demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

//Use ctrl + shift + O to get all imports

public class MemberHello extends Application  {
	int count = 0; 
	Label hiLabel = new Label();
	Button button = new Button("Hello");
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		//some look & feel settings
		hiLabel.setStyle("-fx-font-size:20; -fx-text-fill:blue; "); 
		BorderPane.setAlignment(hiLabel, Pos.CENTER);
		
		/*public class ButtonHandler implements EventHandler<ActionEvent>{ //member class
			@Override
			public void handle(ActionEvent event) {
				hiLabel.setText("Hi " + ++count);
			}
		}*/ 
		//this is local inner class. It has more access but loses usability (can't reuse outside)
		
		button.setOnAction(new ButtonHandler());
		//button.setOnAction(new ButtonHandler(count, hiLabel)); //call for external handler
		
		/*button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hiLabel.setText("Hi " + ++count);
			}
		});*/
		//this is an anonymous class
		//functional interface: interface with only 1 method
		
		//button.setOnAction(arg0 -> hiLabel.setText("Hi " + ++count)); 
		//lambda expression for handler
		
		/*button.setOnAction(arg0 -> {
			hiLabel.setText("Hi " + ++count);
			//can do more things here
		});*/
		//lambda expression with more than 1 line of codes

		//set the scene and the stage
		BorderPane root = new BorderPane();
		root.setCenter(button);
		root.setBottom(hiLabel);
		Scene scene = new Scene(root, 200, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Greetings");
		primaryStage.show();
	}

	public class ButtonHandler implements EventHandler<ActionEvent>{ //member class
		@Override
		public void handle(ActionEvent event) {
			hiLabel.setText("Hi " + ++count);
		}
	}
}
