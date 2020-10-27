//Chi Ngo
//cngongoc

package hw1;

public class GameRound {
	private String puzzleWord;
	private String clueWord;
	private boolean isRoundComplete;
	
	public String getPuzzleWord() {
		return puzzleWord;	//getter for puzzleWord
	}
	
	public void setPuzzleWord(String puzzleWord) {
		this.puzzleWord = puzzleWord;	//setter for puzzleWord, set puzzleWord to a particular word
	}
	
	public String getClueWord() {
		return clueWord;	//return the latest clueWord
	}
	
	public void setClueWord(String clueWord) {
		this.clueWord = clueWord;	//setter for clueWord, set clue to a new clue as it gets updated by the user
	}
	
	public boolean getIsRoundComplete() {
		return isRoundComplete;	//getter for isRoundComplete, may be true or false
	}
	
	public void setIsRoundComplete(boolean isRoundComplete) {
		this.isRoundComplete = isRoundComplete;	//setter for isRoundComplete, set to true or false
	}
}
