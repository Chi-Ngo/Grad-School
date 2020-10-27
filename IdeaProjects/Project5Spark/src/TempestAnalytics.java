import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/*
Chi Ngo - cngongoc
This program checks the file The Tempest
and returns the number of lines, number of words, and number of distinct words.
This program also prompts the user for a string
and outputs all the lines that contain this string
Data processing is done using Spark
 */

public class TempestAnalytics {

    //this method performs the analytical function on the file "The Tempest"
    //the file name is passed as argument
    private static void analytics(String fileName) {
        //this program will connect to Spart thread running on the local host
        //App Name is set to Tempest Analytics
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Tempest Analytics");
        //create SparkContext with the previously set up SparkConfiguration
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        //convert the input file to a JavaRDD object of String
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);
        //the count of this object is the number of lines
        System.out.println("The number of lines in The Tempest is: " + inputFile.count());

        //split the lines by whitespace to get words and put in new JavaRDD object
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        //the count of this object is the number of words
        System.out.println("The number of words in The Tempest is: " + wordsFromFile.count());

        //using the distinct function with wordsFromFile to get only distinct words
        //put distinct words into new JavaRDD object
        JavaRDD<String> distinctWords = wordsFromFile.distinct();
        //the count of this object is the number of distinct words
        System.out.println("The number of distinct words in The Tempest is: " + distinctWords.count());

        //use mapToPair method to map a word, number pair
        //each word has a number value of 1
        //Output to JavaPairRDD object
        JavaPairRDD task3 = wordsFromFile.mapToPair(t -> new Tuple2(t, 1));
        //save the output file as a text file in the specified directory
        task3.saveAsTextFile("P/TheTempestOutputDir1");

        //value of the same key are added together to find the total occurrences of a word
        JavaPairRDD task4 = task3.reduceByKey((x, y) -> (int) x + (int) y);
        //save the output file as a text file in the specified directory
        task4.saveAsTextFile("P/TheTempestOutputDir2");

        //create new BufferedReader to take input from user
        BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
        //prompt the user for a string
        System.out.println("Enter a string:");
        try {
            //read the user's input
            String input = typed.readLine();
            //for each line in the file
            inputFile.foreach( str ->  {
                //if the line has the input string
                if (str.contains(input)) {
                    //print out the whole line
                    System.out.println(str);
                }
            });
            //handle IOexception
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //main method to start the program
    public static void main(String[] args) {
        //if no file name is provided
        if (args.length == 0) {
            //inform the user
            System.out.println("No files provided.");
            //exit the program
            System.exit(0);
        }
        //if a file name is provided
        //pass the file name to analytics to analyze
        analytics(args[0]);
    }
}
