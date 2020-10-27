//Chi Ngo
//cngongoc

package hw3;


public class Hangman extends Game{
	static final int MIN_WORD_LENGTH = 5; //minimum length of puzzle word
	static final int MAX_WORD_LENGTH = 10; //maximum length of puzzle word
	static final int HANGMAN_TRIALS = 10;  // max number of trials in a game
	static final int HANGMAN_GAME_TIME = 30; // max time in seconds for one round of game
	
	HangmanRound hangmanRound;
	
	
	/** setupRound() is a replacement of findPuzzleWord() in HW1. 
	 * It returns a new HangmanRound instance with puzzleWord initialized randomly drawn from wordsFromFile.
	* The puzzleWord must be a word between HANGMAN_MIN_WORD_LENGTH and HANGMAN_MAX_WORD_LEGTH. 
	* Other properties of Hangmanround are also initialized here. 
	*/
	@Override
	HangmanRound setupRound() {
		hangmanRound = new HangmanRound();
		int len = 0;
		String randomWord = new String(); 
		while (len <= MIN_WORD_LENGTH || len >= MAX_WORD_LENGTH) {
			randomWord = WordNerdModel.wordsFromFile[(int) (Math.random() * WordNerdModel.wordsFromFile.length)].trim();
			len = randomWord.length();
		} //this loop will find a word of length between 5 and 10 characters. 
		//If the randomWord is not between these lengths, it will keep running
		hangmanRound.setPuzzleWord(randomWord); //set the puzzleWord as randomWord
		hangmanRound.setIsRoundComplete(false);
		hangmanRound.setClueWord(makeAClue(randomWord)); //make a clue out of randomWord
		return hangmanRound;
	}
	
	
	/** Returns a clue that has at least half the number of letters in puzzleWord replaced with dashes.
	* The replacement should stop as soon as number of dashes equals or exceeds 50% of total word length. 
	* Note that repeating letters will need to be replaced together.
	* For example, in 'apple', if replacing p, then both 'p's need to be replaced to make it a--le */
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

	/** countDashes() returns the number of dashes in a clue String */ 
	int countDashes(String word) {
		int count = 0;
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == '_') {
				count += 1;
			}
		} //loop through the length of the word, if the character is a dash, increase count
		return count;
	}
	
	/** getScoreString() returns a formatted String with calculated score to be displayed after
	 * each trial in Hangman. See the handout and the video clips for specific format of the string. */
	@Override
	String getScoreString() {
		float score;
		if (hangmanRound.getMissCount() == 0) {
			score = hangmanRound.getHitCount(); //if missCount is 0, score = hitCount
		}
		else {
			score = (float) hangmanRound.getHitCount()/hangmanRound.getMissCount();
		}
		String label = 	"Hit: " + hangmanRound.getHitCount() + 
						" Miss: " + hangmanRound.getMissCount() + 
						". Score: " + String.format("%.2f", score);
		return label;
	}

	/** nextTry() takes next guess and updates hitCount, missCount, and clueWord in hangmanRound. 
	* Returns INDEX for one of the images defined in GameView (SMILEY_INDEX, THUMBS_UP_INDEX...etc. 
	* The key change from HW1 is that because the keyboardButtons will be disabled after the player clicks on them, 
	* there is no need to track the previous guesses made in userInputs*/
	@Override
	int nextTry(String guess) {
		int code = -1; 
		String clue = hangmanRound.getClueWord(); 
		String puzzle = hangmanRound.getPuzzleWord();
		int hit = hangmanRound.getHitCount();
		int miss = hangmanRound.getMissCount();
		if (puzzle.contains(guess)) { //if the puzzle contains the guess character
			code = GameView.THUMBS_UP_INDEX; //the guess is correct
			for (int i = 0; i < puzzle.length(); i++) {
				if (puzzle.charAt(i) == guess.toCharArray()[0]) {
					clue = clue.substring(0, i) + guess 
							+ clue.substring(i + 1);
				}
			} //loop through the puzzle word to find all instances of the guess character
			//then replace the appropriate dashes in clue with the guess
			hangmanRound.setClueWord(clue); //set the new clue word
			hit += 1; //update hit as the guess is right
		}
		else {
			code = GameView.THUMBS_DOWN_INDEX; //else, the guess is wrong
			miss += 1; //update miss as the guess is wrong
		}
		hangmanRound.setHitCount(hit); //update the hit count
		hangmanRound.setMissCount(miss); //update the miss count
		String clue1 = hangmanRound.getClueWord(); 
		String puzzle1 = hangmanRound.getPuzzleWord();
		if (hit + miss == HANGMAN_TRIALS) { //if we reach 10 trials, the round is completed
			hangmanRound.setIsRoundComplete(true);
			code = GameView.SADLY_INDEX;
		}
		if (clue1.equals(puzzle1)) { //if clue and puzzle are the same, user wins
			hangmanRound.setIsRoundComplete(true);
			code = GameView.SMILEY_INDEX;
		}
		return code;
	}
}
