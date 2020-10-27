package week8;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ButtonHandler implements EventHandler <ActionEvent>{
	Rectangle color;
	ButtonHandler(Rectangle color) {
		this.color = color;
	}
	@Override
	public void handle(ActionEvent event) {
		Button b = (Button)event.getSource();
		switch (b.getText()) {
		case "Blue": color.setFill(Color.BLUE); break;
		case "Red": color.setFill(Color.RED); break;
		default: break;
		}
	}
}

