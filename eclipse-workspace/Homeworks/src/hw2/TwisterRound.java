//Chi Ngo
//cngongoc

package hw2;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TwisterRound extends GameRound {
	private ObservableList<String> solutionWordsList;
	private ObservableList<ObservableList<String>> submittedListsByWordLength;
	private ObservableList<ObservableList<String>> solutionListsByWordLength;
	
	TwisterRound() {
		//initialize solutionWordsList, submittedListsByWordLength, solutionListsByWordLength
		solutionWordsList = FXCollections.observableArrayList();
		submittedListsByWordLength = FXCollections.observableArrayList();
		solutionListsByWordLength = FXCollections.observableArrayList();
		int size = Twister.TWISTER_MAX_WORD_LENGTH - Twister.TWISTER_MIN_WORD_LENGTH + 1;
		for (int i = 0; i < size; i++) {
			//add the list to make list of lists
			submittedListsByWordLength.add(FXCollections.observableArrayList());
			solutionListsByWordLength.add(FXCollections.observableArrayList());
		}
	}
	
	//set solution list according to the new puzzle word
	public void setSolutionWordsList(List<String> solutionWordsList) {
		this.solutionWordsList = FXCollections.observableArrayList(solutionWordsList);
	}
	
	//return the full solutionWordsList
	public List<String> getSolutionWordsList() {
		return solutionWordsList;
	}
	
	//return the property
	public ObservableList<String> solutionWordsListProperty() {
		return solutionWordsList;
	}
	
	//set the word to the appropriate list
	public void setSubmittedListsByWordLength(String word) {
		int length = word.length();
		submittedListsByWordLength.get(length - Twister.TWISTER_MIN_WORD_LENGTH).add(word);
	}
	
	//return the list of lists
	public ObservableList<ObservableList<String>> getSubmittedListsByWordLength() {
		return submittedListsByWordLength;
	}
	
	//return the specific list
	public ObservableList<String> getSubmittedListsByWordLength(int letterCount) {
		return submittedListsByWordLength.get(letterCount);
	}
	
	//return the property
	public ObservableList<ObservableList<String>> submittedListsByWordLengthProperty() {
		return submittedListsByWordLength;
	}
	
	//set the word to the appropriate list
	public void setSolutionListsByWordLength(String word) {
		int index = word.length() - Twister.TWISTER_MIN_WORD_LENGTH;
		solutionListsByWordLength.get(index).add(word);
	}
	
	//return the list of lists
	public ObservableList<ObservableList<String>> getSolutionListsByWordLength() {
		return solutionListsByWordLength;
	}
	
	//return the appropriate list
	public ObservableList<String> getSolutionListsByWordLength(int letterCount) {
		return solutionListsByWordLength.get(letterCount);
	}
	
	//return the property
	public ObservableList<ObservableList<String>> solutionListsByWordLengthProperty() {
		return solutionListsByWordLength;
	}
}
