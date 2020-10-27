//Chi Ngo
//cngongoc

package hw1;

import java.util.Scanner;

public class WordNerd {
	
	//use these two constants for game choices and avoid using literals 
	public final static int HANGMAN_GAME_ID = 1;
	public final static int TWISTER_GAME_ID = 2;

	static Scanner input = new Scanner(System.in);  //to be used across all classes for keyboard inputs

	//invokes getGameChoice and playWordNerd methods as needed
	// to display the menu and get the game started
	public static void main(String[] args) {
		WordNerd game = new WordNerd(); //create a new WordNerd game
		int choice = game.getGameChoice(); //returns the choice for a game
		game.playWordNerd(choice); //play the choice accordingly
	}

	//Displays the menu and returns the player's choice as 
	//1 (for Hangman), 2 (for Twister), and 3 (for Exit).
	private int getGameChoice() {
		System.out.println("Welcome to WordNerd!");
		System.out.println("----------------------");
		System.out.println("1. Hangman");
		System.out.println("2. Twister");
		System.out.println("3. Exit");
		System.out.println("----------------------");
		int choice = input.nextInt();
		return choice; 
	}

	//given a gameID, starts the game. 
	//For HW1, if gameId is 2, prints the message **** Coming Soon ****
	private void playWordNerd(int gameId) {
		if (gameId == HANGMAN_GAME_ID) {
			Hangman hang = new Hangman(); //initialize a new Hangman game
			hang.startGame(); //start the game
		}
		else if (gameId == TWISTER_GAME_ID) {
			System.out.println("**** Coming Soon ****");
		}
		else {
			System.out.println("Bye Bye!");
		}
	}
}
