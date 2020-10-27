//Chi Ngo
//cngongoc

package hw1;

public class Hangman extends Game {

	static final int MIN_WORD_LENGTH = 5;
	static final int MAX_WORD_LENGTH = 10;
	static final int HANGMAN_TRIALS = 10; 

	//codes returned by nextTry() method for printing appropriate message
	
	static final int RIGHT_MESSAGE_INDEX = 0;
	static final int WRONG_MESSAGE_INDEX = 1;
	static final int ALREADY_ENTERED_MESSAGE_INDEX = 2;
	static final int PART_OF_CLUE_MESSAGE_INDEX = 3;
	static final int CONGRATULATIONS_MESSAGE_INDEX = 4;
	static final int LOST_MESSAGE_INDEX = 5;
	

	String[] messagesArray = { ":-) You got that right!", //RIGHT_MESSAGE_INDEX
			":-( Sorry! Got it wrong!", 		//WRONG_MESSAGE_INDEX
			":-o You already entered that!", 	//ALREADY_ENTERED_MESSAGE_INDEX
			":-\\ Part of the clue!", 			//PART_OF_CLUE_MESSAGE_INDEX
			":-D Congratulations! You won!",   	//CONGRATULATIONS_MESSAGE_INDEX
			":-( Sorry! You lost this one!"}; 	//LOST_MESSAGE_INDEX


	HangmanRound hangmanRound;

	//create and initialize a new round, play, and then print final result
	@Override
	void startGame() {
		hangmanRound = new HangmanRound(); //initialize a new Hangman round
		String word = findPuzzleWord(); //word stores the random word of length between 5 and 10 characters
		hangmanRound.setPuzzleWord(word); //make this word the puzzle word
		String clue = makeAClue(word); //create a clue that has at least 50% of its length as dashes from the puzzle word
		hangmanRound.setClueWord(clue); //make this clue the clue word
		playRound(); 
		float score = getScore(); //calculate and store the score in score
		System.out.print("Your score is ");
		System.out.printf("%.2f", score);
		System.out.println();
		System.out.println("Bye Bye!");
	}

	//return a new puzzle word randomly selected from the Game's wordsFromFile. 
	@Override
	String findPuzzleWord() {
		int len = 0;
		String randomWord = new String(); 
		while (len <= MIN_WORD_LENGTH || len >= MAX_WORD_LENGTH) {
			randomWord = wordsFromFile[(int) (Math.random() * wordsFromFile.length)].trim();
			len = randomWord.length();
		} //this loop will find a word of length between 5 and 10 characters. 
		//If the randomWord is not between these lengths, it will keep running
		return randomWord;
	}

	//Runs a complete round, invoking nextTry() for each guess to be made by the player
	//Keeps track all inputs made by the player
	//A round goes as long as player has not guessed the complete puzzleWord 
	//and the number of guesses is less than HANGMAN_TRIALS
	//Prints the message from  messagesArray[] based on the code returned by nextTry()
	@Override
	void playRound() {
		StringBuilder userInputs = new StringBuilder(); 
		String clue = hangmanRound.getClueWord(); 
		while (!hangmanRound.getIsRoundComplete()) { //The loop keeps running if isRoundComplete is false
			int trial = hangmanRound.getHitCount()+hangmanRound.getMissCount() + 1; //find the number of trials done
			System.out.println("The clue is: " + clue); 
			System.out.print("***Trial# " + trial + ". Enter your guess: ");
			char guess = WordNerd.input.next().charAt(0); //receive the guess character from the user
			int code = nextTry(guess,userInputs); //finds the code for the response using nextTry
			clue = hangmanRound.getClueWord(); //get the updated clue word
			System.out.println(messagesArray[code]); //print out the apporiate message based on code
			if (trial == HANGMAN_TRIALS 
					|| hangmanRound.getClueWord().equals(hangmanRound.getPuzzleWord())) {
				hangmanRound.setIsRoundComplete(true);
			} //if the number of trials is 10 or clue is the same as the puzzle word, the round is complete
		}
		System.out.println("The word is: " + hangmanRound.getPuzzleWord());
		if (countDashes(clue) == 0) { 
			System.out.println(messagesArray[CONGRATULATIONS_MESSAGE_INDEX]);
		} //after the round is complete, if there is no dash left, the word has been guessed correctly
		else {
			System.out.println(messagesArray[LOST_MESSAGE_INDEX]);
		} //if there are still dashes, the player has ran out of trials and lost
	}

	//Takes next guess and prior userInputs. 
	//updates hitCount, missCount, and clueWord in hangmanRound
	//updates userInputs
	//returns code for message to be printed
	int nextTry(char guess, StringBuilder userInputs) {
		int code = -1; 
		String clue = hangmanRound.getClueWord(); 
		String puzzle = hangmanRound.getPuzzleWord();
		int hit = hangmanRound.getHitCount();
		int miss = hangmanRound.getMissCount();
		if (clue.contains(Character.toString(guess))) {
			code = PART_OF_CLUE_MESSAGE_INDEX;
		} //if the guess character is contained in the clue, code is now the index of that message
		//this if statement comes first because if the player makes a correct guess and re-enter it, 
		//the message should be part of the clue and not already entered.
		else if (userInputs.toString().contains(Character.toString(guess))) {
			code = ALREADY_ENTERED_MESSAGE_INDEX; 
		} //if the guess character is contained in the list of all the previous inputs, it has already been entered
		else { //the case of right or wrong guess
			if (puzzle.contains(Character.toString(guess))) { //if the puzzle contains the guess character
				code = RIGHT_MESSAGE_INDEX; 
				for (int i = 0; i < puzzle.length(); i++) {
					if (puzzle.charAt(i) == guess) {
						clue = clue.substring(0, i) + guess 
								+ clue.substring(i + 1);
					}
				} //loop through the puzzle word to find all instances of the guess character
				//then replace the appropriate dashes in clue with the guess
				hangmanRound.setClueWord(clue); //set the new clue word
				hit += 1; //update hit as the guess is right
				userInputs.append(guess); //update userInputs
			}
			else {
				code = WRONG_MESSAGE_INDEX; 
				miss += 1; //update miss as the guess is wrong
				userInputs.append(guess); //update userInputs
			}
		}
		hangmanRound.setHitCount(hit); //update the hit count
		hangmanRound.setMissCount(miss); //update the miss count
		return code;
	}

	
	//Returns a clue that has at least half the number of letters 
	//in puzzleWord replaced with dashes.
	//The replacement should stop as soon as number of dashes equals or exceeds 50% of total word length. 
	//Note that repeating letters will need to be replaced together.
	//For example, in 'apple', if replacing p, then both 'p's need to be replaced to make it a--le
	@Override
	String makeAClue(String puzzleWord) {
		char [] word = puzzleWord.toCharArray(); 
		int len = word.length;
		int dash = 0;
		while ((double) dash/len < 0.5) { //the loop keeps running when less than 50% of the word has been replaced with dashes
			int index  = (int) (Math.random() * len); //pick a random index
			if (word[index] != ('-')) {
				for (int i = 0; i < len; i++) {
					if (word[index] == word[i] && i != index) {
						word[i] = '-';
						dash += 1;
					}
				} //if the character at index is repeated in the word, change that repeated occurrence to dash
				word[index] = '-';//change the character at index to dash
				dash += 1;
			}
		}
		String dashedWord = new String(word); //change the character array to a string
		return dashedWord; //return the string
	}

	//returns the number of dashes in a clue string 
	int countDashes(String word) {
		int count = 0;
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == '-') {
				count += 1;
			}
		} //loop through the length of the word, if the character is a dash, increase count
		return count;
	}

	//returns score based on Hangman rules
	@Override
	float getScore() {
		float score; 
		int miss = hangmanRound.getMissCount(); 
		int hit = hangmanRound.getHitCount();
		if (miss != 0) {
			score = (float) hit/miss;
		} //if miss is not zero, score is hit/miss
		else {
			score = (float) hit;
		} //if miss is zero, cout is just the number of hits
		return score;
	}
}
