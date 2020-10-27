//Chi Ngo
//cngongoc
package hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WordNerdModel {
	static String[] wordsFromFile;
	static String WORDS_FILE_NAME = "data\\wordsfile.txt";
	
	static void readWordsFile(String wordsFilename) {
		StringBuilder contents = new StringBuilder();
		Scanner fileScanner = null;
		try {
			File file = new File(wordsFilename);
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
	}
}
