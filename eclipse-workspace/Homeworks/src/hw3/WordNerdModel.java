//Chi Ngo
//cngongoc
package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WordNerdModel {
	static String[] wordsFromFile;
	static String WORDS_FILE_NAME = "data\\wordsfile.txt";
	static String SCORE_FILE_NAME = "data\\scores.csv";
	ObservableList<Score> scoreList = FXCollections.observableArrayList();
	
	//read wordsFile and throw exceptions when appropriate
	static String readWordsFile(String wordsFilename) {
		StringBuilder contents = new StringBuilder();
		Scanner fileScanner = null;
		File file = null;
		String fileName = new String();
		try {
			file = new File(wordsFilename);
			fileScanner = new Scanner(file);
			fileName = file.getName();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		//throw exception when file is empty or contains illegal characters
		try {
			if (file.length() == 0) {
				throw new InvalidWordSourceException("Check word source format!");
			}
			else {
				while (fileScanner.useDelimiter("\n").hasNext()) { 
					String word = fileScanner.next().trim();
					if (!word.matches("^[a-zA-Z]*$")) {
						throw new InvalidWordSourceException("Check word source format!");
					}
				}
			}
		} catch (InvalidWordSourceException e){
			e.showAlert();
			file = new File(WORDS_FILE_NAME);
			fileName = WORDS_FILE_NAME;
		}
		//read words from the updated wordsFile
		try {
			fileScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fileScanner.useDelimiter("\n").hasNext()) { 
			contents.append(fileScanner.next() + "\n");	
		}
		wordsFromFile = contents.toString().split("\n"); //split the StringBuilder by new line to create an array of lines
		for (int i = 0; i < wordsFromFile.length; i++) {
			wordsFromFile[i] = wordsFromFile[i].trim(); //trim white space from the each line
		}
		return fileName;
	}
	
	//writeScore into scores.csv
	void writeScore(String scoreString) {
		//append new score string to scores.csv
		try (FileWriter fileWriter = new FileWriter(SCORE_FILE_NAME, true);
				PrintWriter printWriter = new PrintWriter(fileWriter)) 
		{
			printWriter.println(scoreString);  
			printWriter.close();
		} catch (IOException e) {
			System.out.println("File does not exist");
			e.printStackTrace();
		} 
	}
	
	//read scores from scores.csv
	void readScore() {
		StringBuilder contents = new StringBuilder();
		Scanner fileScanner = null;
		try {
			File file = new File(SCORE_FILE_NAME);
			fileScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fileScanner.useDelimiter("\n").hasNext()) { 		
			contents.append(fileScanner.next() + "\n");		
		}
		String[] lines = contents.toString().split("\n");
		//store the score into scoreList
		for (int i = scoreList.size(); i < lines.length; i++) {
			 String[] items = lines[i].split(",");
			 //create new score object 
			 Score score = new Score(Integer.parseInt(items[0]), items[1], Integer.parseInt(items[2]), Float.parseFloat(items[3]));
			 scoreList.add(score);
		}
	}
}
