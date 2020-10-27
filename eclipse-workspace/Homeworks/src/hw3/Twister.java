//Chi Ngo
//cngongoc

package hw3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Twister extends Game {
	static int SOLUTION_LIST_COUNT = 5;
	static int TWISTER_MAX_WORD_LENGTH = 7;
	static int TWISTER_MIN_WORD_LENGTH = 3;
	static int NEW_WORD_BUTTON_INDEX = 0;
	static int TWIST_BUTTON_INDEX = 1;
	static int CLEAR_BUTTON_INDEX = 2;
	static int SUBMIT_BUTTON_INDEX = 3;
	static int CLUE_BUTTON_SIZE = 75;
	static int TWISTER_GAME_TIME = 120;
	static int MIN_SOLUTION_WORDCOUNT = 10;
	TwisterRound twisterRound;
	
	@Override
	TwisterRound setupRound() {
		twisterRound = new TwisterRound();
		String randomWord = new String(); 
		List<String> solution = new ArrayList<>(); //list to hold all possible solutions
		boolean valid = false;
		while (valid == false) {
			//get a random word from the file
			randomWord = WordNerdModel.wordsFromFile[(int) (Math.random() * WordNerdModel.wordsFromFile.length)].trim();
			solution = new ArrayList<String>();
			//check if this random word meets length requirements
			if (randomWord.length() >= TWISTER_MIN_WORD_LENGTH && randomWord.length() <= TWISTER_MAX_WORD_LENGTH) {
				solution = new ArrayList<>(); 
				for (String word: WordNerdModel.wordsFromFile) { //for each word in word list. To check for potential solution
					//accept a word only if its length is at least 3 and at most the length of the random word
					if (word.length() >= TWISTER_MIN_WORD_LENGTH && word.length() <= randomWord.length()) {
						if (StringContain(randomWord,word) == true) { //check if this word can be twisted from randomWord
							solution.add(word); //if it can, add to solution list
						}
					}
				}
				if (solution.size() >= MIN_SOLUTION_WORDCOUNT) { //if there are more than 10 solutions, while loop can end
					valid = true;
				}
			}
			
		} 
		twisterRound.setSolutionWordsList(solution); //set the solution as the solutionWordsList
		for (String word : twisterRound.getSolutionWordsList()) {
			twisterRound.setSolutionListsByWordLength(word); //add each word to appropriate solutionListsByWordLength
		}
		twisterRound.setPuzzleWord(randomWord); //set puzzleWord as randomWord
		twisterRound.setClueWord(makeAClue(randomWord)); //shuffle the puzzleWord
		return twisterRound;
	}
	
	boolean StringContain(String longer, String shorter) { //helper method to check if a shorter word can be twisted out of the longer word
		String test = longer;
	    for (char c : shorter.toCharArray()) { 
	       if (!test.contains(Character.toString(c))) { //if the longer word doesn't contain a character in the shorter word, it's false
	    	   return false;
	       }
	       else {
	    	   test = test.replaceFirst(Character.toString(c), "-"); //else, replace the first occurrence with - to check avoid duplicate checking
	       }
	    }
	    return true;
	}
	
	@Override
	String makeAClue(String puzzleWord) { 
		//found Collections.shuffle idea from 
		//https://stackoverflow.com/questions/20588736/how-can-i-shuffle-the-letters-of-a-word
		List<Character> l = new ArrayList<>();
		char [] words = puzzleWord.toCharArray();
		for(int i = 0; i < words.length; i++) {
			l.add(words[i]);
		}
		Collections.shuffle(l); 
		StringBuilder sb = new StringBuilder(); 
		for(char c : l)
		  sb.append(c);
		String clueWord = sb.toString();
		return clueWord;
	}

	@Override
	String getScoreString() {
		String label = "Twist to find " + findLeftOver() + " of " + twisterRound.getSolutionWordsList().size() + " words";
		return label;
	}
	
	int findLeftOver() {
		//helper method to find the number of words left to find
		int total = twisterRound.getSolutionWordsList().size(); //total number of solutions
		int submitted = 0; //number of correct submission
		for (List<String> l: twisterRound.getSubmittedListsByWordLength()) {
			submitted += l.size();
		}
		int left = total - submitted;
		return left;
	}

	@Override
	int nextTry(String guess) {
		int code = -1;
		if (guess.length() < Twister.TWISTER_MIN_WORD_LENGTH) {
			code = GameView.THUMBS_DOWN_INDEX; //if the guess length is less than 3, it's not valid
		}
		else {
			List<String> solution = twisterRound.getSolutionWordsList();
			List<String> submitted = twisterRound.getSubmittedListsByWordLength(guess.length() - TWISTER_MIN_WORD_LENGTH);
			if (submitted.contains(guess)) { //if the guess is part of the submitted list, it's repeated
				code = GameView.REPEAT_INDEX; 
			}
			else if (solution.contains(guess)) { //if the guess is part of solution list and not the submitted list, it's correct
				code = GameView.THUMBS_UP_INDEX;
				twisterRound.setSubmittedListsByWordLength(guess);
				if (findLeftOver() == 0) { //if the player has found all the possible words, they win
					code = GameView.SMILEY_INDEX;
					twisterRound.setIsRoundComplete(true);
				}
			}
			else { //if the guess is not part of the solution list, it's wrong
				code = GameView.THUMBS_DOWN_INDEX;
			}
		}
		return code;
	}

}
