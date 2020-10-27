package week8;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloUser extends Application {
	TextField userTextField = new TextField();
	Text helloMessage = new Text();
	@Override
	public void start(Stage primaryStage) {
		//set grid 
		GridPane grid = new GridPane(); 
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		//add grid to scene
		Scene scene = new Scene(grid, 300, 250);
		//create child nodes
		Text userText = new Text("Enter your name");
		Text userName = new Text("Name: ");
		Button btn = new Button();
		btn.setText("Say Hello");
		btn.setOnAction(new ButtonHandler());
		// add child nodes to grid
		grid.add(userText, 0, 0);
		grid.add(userName,  0, 1);
		grid.add(userTextField, 1, 1);
		grid.add(btn, 0, 2);
		grid.add(helloMessage, 1, 2);
		grid.setGridLinesVisible(true); //set this to false when done
		//set the stage and add the scene
		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
		} //end start
		public static void main(String[] args) {
			launch(args);
		} //end main
		//inner class for event-handling
		private class ButtonHandler implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent arg0) {
				// update helloMessage based on user input
				helloMessage.setText("Hello " + userTextField.getText());
				userTextField.clear(); 
				
			}}} //end HelloUser
