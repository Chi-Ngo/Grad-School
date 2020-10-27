//Chi Ngo
//cngongoc

package hw3;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SearchController extends WordNerdController {
	
	//initialize a new searchView
	SearchView searchView = new SearchView();
	
	@Override
	void startController() {
		//clear the old screen
		WordNerd.root.getChildren().clear();
		//read new scores
		wordNerdModel.readScore();
		searchView.searchTableView.setItems(wordNerdModel.scoreList);
		//set up view using new items
		searchView.setupView();
		setupBindings();
	}

	@Override
	void setupBindings() {
		//binding to act when there is new search string in searchTextField
		searchView.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			//get the searchWord
			String searchWord = searchView.searchTextField.getText(); 
			//create a list of words that contain searchWord
            ObservableList<Score> foundWords = FXCollections.observableArrayList();
            //get the game Id
            String game = searchView.gameComboBox.getValue();
      		for (Score score: wordNerdModel.scoreList) {
      			if (game == "Hangman") {
      				//find words that is from Hangman game and contain searchWord
      				if (containsAllChar(score.getPuzzleWord(), searchWord) == true && score.getGameId() == 0) {
      					foundWords.add(score);
      				}	
      			}
      			else if (game == "Twister") {
      				//find words that is from Twister game and contain searchWord
      				if (containsAllChar(score.getPuzzleWord(), searchWord) == true && score.getGameId() == 1) {
          				foundWords.add(score);
          			}
      			}
      			else {
      				//find words from all games and contain searchWords
      				if (containsAllChar(score.getPuzzleWord(), searchWord) == true) {
          				foundWords.add(score);
          			}
      			}
      		}
      		//set new items to display
      		searchView.searchTableView.setItems(foundWords);
		});
		
		//binding to act when game is changed in gameComboBox
        searchView.gameComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//get new game id and searchWord
				String game = searchView.gameComboBox.getValue();
				String search = searchView.searchTextField.getText();
				ObservableList<Score> foundWords = FXCollections.observableArrayList();
				if (game == "Hangman") {
					for (Score score: wordNerdModel.scoreList) {
						//found words that is from Hangman and contains searchWord
						if (score.getGameId() == 0 && containsAllChar(score.getPuzzleWord(),search)) {
							foundWords.add(score);
						}
					}
					//display the words
					searchView.searchTableView.setItems(foundWords);
				}
				else if (game == "Twister") {
					for (Score score: wordNerdModel.scoreList) {
						//found words that is from Twister and contains searchWord
						if (score.getGameId() == 1 && containsAllChar(score.getPuzzleWord(),search)) {
							foundWords.add(score);
						}
					}
					//display the words
					searchView.searchTableView.setItems(foundWords);
				}
				else {
					for (Score score: wordNerdModel.scoreList) {
						//found words that is from all games and contains searchWord
						if (containsAllChar(score.getPuzzleWord(),search)) {
							foundWords.add(score);
						}
					}
					//display the words
					searchView.searchTableView.setItems(foundWords);
				}
				
		}});
		
	}
	
	//helper method to check if a word contains all characters of a search string
	boolean containsAllChar (String tester, String testee) {
		boolean contain = true;
		for (int j = 0; j < testee.length(); j++) {
			if (tester.toLowerCase().indexOf(testee.toLowerCase().charAt(j)) == -1) {
				contain = false;
				break;
			}
		}
		return contain;
	}

}
