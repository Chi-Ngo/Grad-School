package week8;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

//two stages, two buttons, two roots

public class MultiStage extends Application {
	BorderPane helloRoot = new BorderPane();
	BorderPane byeRoot = new BorderPane();
	Stage byeStage = new Stage();
	Scene helloScene, byeScene;
	
	public static void main(String[] args) { 
		launch(args); 
	}
	@Override
	public void start(Stage helloStage) throws Exception { //must implement this
		setStages();
		helloScene = new Scene(helloRoot, 200, 100); //create Scene from Root
		byeScene = new Scene(byeRoot, 200, 100);
		helloStage.setTitle("Hello Stage");
		helloStage.setScene(helloScene); //put Scene on Stage
		helloStage.show(); //show Stage
	}
	void setStages() {
		Button helloButton = new Button("Hello!");
		helloRoot.setCenter(helloButton);
		helloButton.setOnAction(new HelloButtonHandler()); //registering the button its respective handler
		//this gets created and registered in memory
		//handler gets called when button is clicked

		Button byeButton = new Button("Bye!");
		byeRoot.setCenter(byeButton);
		byeStage.setTitle("Hello Stage");
		byeButton.setOnAction(new ByeButtonHandler());
	}
	private class HelloButtonHandler implements EventHandler<ActionEvent>{ //Do things to byeStage
		@Override
		public void handle(ActionEvent event) {
			byeStage.setX(500);
			byeStage.setY(300);
			byeStage.setScene(byeScene);
			byeStage.show();
		}
	}
	private class ByeButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			byeStage.close();
		}
	}
}

