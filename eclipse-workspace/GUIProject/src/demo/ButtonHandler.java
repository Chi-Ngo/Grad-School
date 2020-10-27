package demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class ButtonHandler implements EventHandler<ActionEvent>{ //external class
	int count; 
	Label hiLabel;
	
	ButtonHandler(int count, Label hiLabel) {
		this.count = count;
		this.hiLabel = hiLabel;
	}
	@Override
	public void handle(ActionEvent event) {
		hiLabel.setText("Hi " + ++count);
	}
}
