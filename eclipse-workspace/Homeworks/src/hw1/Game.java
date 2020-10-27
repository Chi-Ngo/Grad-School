//Chi Ngo
//cngongoc

package hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class Game {
	public static final String WORDS_FILE_NAME = "wordsFile.txt";  //use this constant wherever file name is used. 
	public static String[] wordsFromFile;		//stores words read from the word file in this array

	Game() {
		wordsFromFile = readFile(WORDS_FILE_NAME);
	} //default constructor supposed to read and store all the words from file

	//readfile() returns an array of words read from the file
	public static String[] readFile(String filename) {
		StringBuilder contents = new StringBuilder();
		Scanner fileScanner = null;
		try {
			File file = new File(filename);
			fileScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fileScanner.useDelimiter("\n").hasNext()) { 		
			contents.append(fileScanner.next() + "\n");		
		}
		String [] words = contents.toString().split("\n");
		return words;
	}

	abstract void startGame();
	abstract String findPuzzleWord();
	abstract void playRound();
	abstract String makeAClue(String puzzleWord);
	abstract float getScore();
}
