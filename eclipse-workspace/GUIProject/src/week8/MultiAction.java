package week8;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MultiAction extends Application{
	public static void main(String[] args) {
		launch(args);
	}
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		Button b = new Button("Java");
		b.addEventHandler(Event.ANY, new AnyActionHandler());
		
		b.addEventHandler(MouseEvent.MOUSE_ENTERED, new ButtonEnterHandler());
		b.addEventHandler(MouseEvent.MOUSE_EXITED, new ButtonExitHandler());

		b.setOnMouseDragged(new ButtonDragHandler());
		b.setOnAction(new ButtonClickHandler());

		root.setCenter(b); 
		Scene s = new Scene(root, 100, 100); 
		primaryStage.setScene(s); primaryStage.show();
	}
	private class AnyActionHandler implements EventHandler<Event>  {
		public void handle(Event event) {
			Button b = (Button)event.getSource();
			b.setRotate(b.getRotate() + 90);
		} 
	} //this handler can be tied to more than 1 component
	
	private class ButtonEnterHandler implements EventHandler<MouseEvent>  {
		public void handle(MouseEvent event) {
			Button b = (Button)event.getSource();
			b.setStyle("-fx-color: red");
		}
	}
	
	private class ButtonExitHandler implements EventHandler<MouseEvent>  {
		public void handle(MouseEvent event) {
			Button b = (Button)event.getSource();
			b.setStyle("-fx-color: whitesmoke");
		}
	}
	
	private class ButtonDragHandler implements EventHandler<MouseEvent>  {
		public void handle(MouseEvent event) {
			Button b = (Button)event.getSource();
			b.setStyle("-fx-color: yellow");
		}
	}
	
	private class ButtonClickHandler implements EventHandler<ActionEvent>  {
		public void handle(ActionEvent event) {
			Button b = (Button)event.getSource();
			b.setStyle("-fx-color: lightgreen");
		}	
	}		
}
