package edu.cmu.cngongoc.p1t3;

public class DSClickerModel {
    //initialize an array of size 4 to store the count of each choice
    //count of choice A is stored in freq[0]
    //B in freq[1]
    //C in freq[2]
    //D in freq[3]
    int[] freq = new int[4];

    //using the submitted choice, increment the appropriate count
    public void addScore(String choice) {
        if (choice.equals("A")) {
            freq[0] += 1;
        }
        else if (choice.equals("B")) {
            freq[1] += 1;
        }
        else if (choice.equals("C")) {
            freq[2] += 1;
        }
        else {
            freq[3] += 1;
        }
    }

    //return the current freq array
    public int[] getFreq() {
        return freq;
    }

    //clear all past results
    public void clearFreq() {
        freq[0] = 0;
        freq[1] = 0;
        freq[2] = 0;
        freq[3] = 0;
    }
}
