//Chi Ngo
//cngongoc

package hw2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class TwisterController extends WordNerdController {
	TwisterView twisterView;
	Twister twister;
	
	@Override
	void startController() {
		twisterView = new TwisterView(); //create a new view
		twister = new Twister(); 
		twister.twisterRound = twister.setupRound(); //set up a new round
		twisterView.refreshGameRoundView(twister.twisterRound); //refresh the view
		setupBindings();
		
		VBox lowerPanel = new VBox();
		lowerPanel.getChildren().add(twisterView.bottomGrid);
		lowerPanel.getChildren().add(WordNerd.exitButton);
		lowerPanel.setAlignment(Pos.CENTER);

		WordNerd.root.setTop(twisterView.topMessageText);
		WordNerd.root.setCenter(twisterView.topGrid);
		WordNerd.root.setBottom(lowerPanel);
		
		//if New Word button is clicked, refresh the view and set up a new round
		//restart the timer 
		//set up new bindings
		twisterView.playButtons[0].setOnAction(event -> { 
			twister.twisterRound = twister.setupRound();
			twisterView.refreshGameRoundView(twister.twisterRound);
			GameView.wordTimer.restart(Twister.TWISTER_GAME_TIME);
			setupBindings();
		});
		
		//bind the handlers to their buttons
		twisterView.playButtons[0].setOnAction(new NewButtonHandler());
		twisterView.playButtons[1].setOnAction(new TwistButtonHandler());
		twisterView.playButtons[2].setOnAction(new ClearButtonHandler());
		twisterView.playButtons[3].setOnAction(new SubmitButtonHandler());
		for (int i = 0; i < twisterView.clueButtons.length; i++) {
			twisterView.clueButtons[i].setOnAction(new ClueButtonHandler());
			twisterView.answerButtons[i].setOnAction(new AnswerButtonHandler());
		}
	}
	
	@Override
	void setupBindings() {
		//if clue word changes, change each clue word button and reset answer buttons
		twister.twisterRound.clueWordProperty().addListener((observable, oldValue, newValue) -> {
			for (int i = 0; i < twister.twisterRound.getClueWord().length(); i++) {
				twisterView.clueButtons[i].setText(String.format("%s", newValue.charAt(i)));
				twisterView.answerButtons[i].setText("");
			}
		});
		
		//When timer runs out, set smiley to sadly, isRoundComplete to true
		GameView.wordTimer.timeline.setOnFinished(event -> { 
		twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[GameView.SADLY_INDEX]);
		twister.twisterRound.setIsRoundComplete(true);

		});
		
		//when the round is completed, disable clueButtons, answerButtons, Twist, Clear, Submit buttons
		twisterView.clueGrid.disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());
		twisterView.playButtons[1].disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());
		twisterView.playButtons[2].disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());
		twisterView.playButtons[3].disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());
	}
	
	class TwistButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			String newWord = new String();
			for (int i = 0; i < twisterView.clueButtons.length; i++) {
				if (!twisterView.clueButtons[i].getText().equals("")) { //if there is a letter at the clueButton
					newWord = newWord + twisterView.clueButtons[i].getText(); //append it to the word
					twisterView.clueButtons[i].setText("");
				}
			}
			String twisty = twister.makeAClue(newWord); //make a new clue/shuffle the word again
			for (int i = 0; i < newWord.length(); i++) {
				twisterView.clueButtons[i].setText(Character.toString(twisty.charAt(i))); //set the new clue to clueButtons
			}
		}
	}
	
	class ClueButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			String alphabet = ((Button)event.getSource()).getText(); //get the clicked character
			for (int i = 0; i < twisterView.answerButtons.length; i++) {
				if (twisterView.answerButtons[i].getText().equals("") ) { //find first blank answerButton
					twisterView.answerButtons[i].setText(alphabet); //set the character to first empty answerButton
					break;
				}
			}
			((Button)event.getSource()).setText(""); //set the clicked button to blank
		}
	}
	
	class AnswerButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			String alphabet = ((Button)event.getSource()).getText(); //get the clicked character
			for (int i = 0; i < twisterView.clueButtons.length; i++) {
				if (twisterView.clueButtons[i].getText().equals("") ) { //find first blank clueButton
					twisterView.clueButtons[i].setText(alphabet); //set the character to first empty clueButton
					break;
				}
			}
			((Button)event.getSource()).setText(""); //set the clicked button to blank
		}
	}
	
	class ClearButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			String alphabet = new String();
			for (int i = 0; i < twisterView.answerButtons.length; i++) {
				if (!twisterView.answerButtons[i].getText().equals("")) { //if there is a character at the answerButton
					alphabet = alphabet + twisterView.answerButtons[i].getText(); //get the character at each answerButton
					twisterView.answerButtons[i].setText(""); //set the answerButton to blank
				}
			}
			int index = 0;
			for (int i = 0; i < twisterView.clueButtons.length; i++) {
				if (twisterView.clueButtons[i].getText().equals("")) { //if the clueButton is blank
					twisterView.clueButtons[i].setText(Character.toString(alphabet.charAt(index))); //insert the character
					index += 1;
				}
			}
		}
	}
	
	class SubmitButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			String alphabet = new String();
			//if answerButton is not blank, get the character from it and append them to make a word
			for (int i = 0; i < twisterView.answerButtons.length; i++) {
				if (!twisterView.answerButtons[i].getText().equals("")) {
					alphabet += twisterView.answerButtons[i].getText();
				}
			}
			//check whether the word is right using nextTry
			int index = twister.nextTry(alphabet);
			//using index, set the appropriate graphic
			twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[index]);
			//the word is right or the game is complete
			if (index == GameView.THUMBS_UP_INDEX || index == GameView.SMILEY_INDEX) {
				//add the word to its corresponding solutionListViews and sort this view
				twisterView.solutionListViews[alphabet.length()-Twister.TWISTER_MIN_WORD_LENGTH].setItems
					(twister.twisterRound.getSubmittedListsByWordLength(alphabet.length()-Twister.TWISTER_MIN_WORD_LENGTH).sorted());
				//update the wordScoreLabels using the new length of submittedListsByWordLength 
				twisterView.wordScoreLabels[alphabet.length() - Twister.TWISTER_MIN_WORD_LENGTH].setText(
					(twister.twisterRound.getSubmittedListsByWordLength(alphabet.length() - Twister.TWISTER_MIN_WORD_LENGTH).size() 
					+ "/" + twister.twisterRound.getSolutionListsByWordLength(alphabet.length() - Twister.TWISTER_MIN_WORD_LENGTH).size()));
				//update the topMessageText with the new score string
				//do TA's actually read all these comments?
				twisterView.topMessageText.setText(twister.getScoreString()); //set the new score 
				String clue = twister.makeAClue(twister.twisterRound.getPuzzleWord()); //shuffle the clue again
				for (int i = 0; i < twisterView.clueButtons.length; i++) {
					twisterView.answerButtons[i].setText(""); //clear all answerButtons
					twisterView.clueButtons[i].setText(Character.toString(clue.charAt(i))); //set up new clueButtons
				}
			}
			//if the game is complete
			if (index == GameView.SMILEY_INDEX) {
				//stop the timer
				GameView.wordTimer.timeline.stop();
				//set isRoundComplete to true
				twister.twisterRound.setIsRoundComplete(true);
			}
		}
	}
	
	class NewButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			GameView.wordTimer.timeline.stop(); //stop the timer
			startController(); //start a new controller
		}
	}
}
