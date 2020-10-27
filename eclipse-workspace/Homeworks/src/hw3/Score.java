//Chi Ngo
//cngongoc

package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Score {
	private IntegerProperty gameId;
	private StringProperty puzzleWord;
	private IntegerProperty timeStamp;
	private FloatProperty score;
	
	//set up default constructor
	public Score() {
		gameId = new SimpleIntegerProperty();
		puzzleWord = new SimpleStringProperty();
		timeStamp = new SimpleIntegerProperty();
		score = new SimpleFloatProperty();
	}
	
	//set up constructor with parameters
	public Score(int gameId, String puzzleWord, int timeStamp, float score) {
		this.gameId = new SimpleIntegerProperty();
		this.puzzleWord = new SimpleStringProperty();
		this.timeStamp = new SimpleIntegerProperty();
		this.score = new SimpleFloatProperty();
		this.gameId.set(gameId);
		this.puzzleWord.set(puzzleWord);
		this.timeStamp.set(timeStamp);
		this.score.set(score);
	}
	
	//setters and getters for gameId
	public void setGameId(int gameId) { this.gameId.set(gameId); }
	public int getGameId() { return gameId.get(); }
	public IntegerProperty gameIdProperty() { return gameId; }
	
	//setters and getters for puzzleWord
	public void setPuzzleWord(String puzzleWord) { this.puzzleWord.set(puzzleWord); }
	public String getPuzzleWord() { return puzzleWord.get(); }
	public StringProperty puzzleWordProperty() { return puzzleWord; }

	//setters and getters for timeStamp
	public int getTimeStamp() { return timeStamp.get(); }
	public void setTimeStamp(int timeStamp) { this.timeStamp.set(timeStamp); }
	public IntegerProperty clueWordProperty() { return timeStamp; }
	
	//setters and getters for score
	public float getScore() { 	return score.get(); }
	public void setScore(float score) { this.score.set(score); }
	public FloatProperty scoreProperty() { return score; }
}
