package LanguageRecogniserPackage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Janice Brogan
 * @Version 1.0
 */

/*
 * Date created: 24/04/2015
 * Date last modified: 14/04/2015
 * Link to project screencording: https://youtu.be/hZ_YBXaMnJI
 */

/**
 * This class features the instance variables and methods associated with building a probabilistic model of a language,
 * based on the bigram counts of characters found in a given input 'training' text representing the language in question.
 * This model features the following attributes: all characters found in the the training text; the total number of 
 * characters in the training text; the number of times a given character occurs in the training text; the number of times 
 * a given bigram occurs in the training text; the probability that a given bigram occurs in the training text.
 */
public class LanguageModel {

	private ArrayList<Character> inputChars;
	private int charCount;
	private HashMap<Character, Double> unigramFreqMap;
	private HashMap<String, Double> bigramFreqMap;
	private HashMap<String, Double> bigramProbsMap;

	/**
	 * This is the constructor that creates a LanguageModel object. Its instance variables are initialised to that
	 * which is returned by calling the instance methods of the class.
	 * 
	 * @param inputFilePath the String sent to the constructor from the driver class containing the training text file name
	 * @throws Exception 	
	 */
	public LanguageModel(String inputFilePath) throws Exception {

		try {

			inputChars = storeInputChars(inputFilePath);
			charCount = countInputChars();
			unigramFreqMap = storeUnigramFreq();
			bigramFreqMap = storeBigramFreq();
			bigramProbsMap = calculateBigramProbs();

		}
		catch (Exception e) {
			System.out.println("Exception was thrown.");
			System.out.println("Exception message: " + e.getMessage());
		}
	}

	/**
	 * This instance method reads in all characters that are contained in the given input text file and stores each one
	 * in an ArrayList<Character>, i.e. the training text is represented as ArrayList<Character>.
	 * 
	 * @param inputFilePath the String that contains the file path name of the input file to be read in
	 * @return 			 an ArrayList<Character> containing the characters found in the the input text file
	 * @throws IOException  
	 */
	public ArrayList<Character> storeInputChars (String inputFilePath) throws IOException {

		String inputPath = System.getProperty("user.dir") + File.separator + inputFilePath;

		ArrayList<Character> charArrList = new ArrayList<Character>();
		char[] charArray;

		Scanner scan = null;

		try {
			scan = new Scanner(new File(inputPath));

			while (scan.hasNext()) {
				charArray = scan.nextLine().toLowerCase().toCharArray();
				for (Character c : charArray) {
					charArrList.add(c);
				}
			}
		} 
		catch (IOException e) {
			System.out.println("IOException was thrown.");
			System.out.println("Exception message: " + e.getMessage());
		}
		catch (Exception e) {
			System.out.println("Exception was thrown.");
			System.out.println("Exception message: " + e.getMessage());
		}

		scan.close(); 

		return charArrList;
	}

	/**
	 * @return an int value representing the total number of characters in the training text		
	 */
	public int countInputChars () {

		int totalCharCount = inputChars.size();

		return totalCharCount;
	}

	/**
	 * This instance method uses a HashMap<Character, Double> to store all alphabetic characters and the number of times 
	 * each one occurs in the training text. First, all characters (a-z), including a whitespace, are stored in the HashMap. 
	 * Each character is stored as the HashMap's key, and for each key, the corresponding value (to represent character 
	 * frequency) is initially set to 0.0. The method then iterates through the inputChars ArrayList (containing the training 
	 * text file). Each time a character in this ArrayList is found to match a key in the HashMap, the value of that key is 
	 * incremented by 1.0.
	 * 
	 * @return a HashMap<Character, Double> containing the characters (as key) in the alphabet, including a whitespace, 
	 * 		and the corresponding number of times (as value) a given character occurs in the training text
	 */
	public HashMap<Character, Double> storeUnigramFreq() {

		HashMap<Character, Double> unigramMap = new HashMap<Character, Double>();
		char singleLetter;
		Double freq;

		// Store all alphabetic characters and a whitespace (with initial 0.0 frequency) in the map
		unigramMap.put(' ', 0.0);
		unigramMap.put('a', 0.0);
		unigramMap.put('b', 0.0);
		unigramMap.put('c', 0.0);
		unigramMap.put('d', 0.0);
		unigramMap.put('e', 0.0);
		unigramMap.put('f', 0.0);
		unigramMap.put('g', 0.0);
		unigramMap.put('h', 0.0);
		unigramMap.put('i', 0.0);
		unigramMap.put('j', 0.0);
		unigramMap.put('k', 0.0);
		unigramMap.put('l', 0.0);
		unigramMap.put('m', 0.0);
		unigramMap.put('n', 0.0);
		unigramMap.put('o', 0.0);
		unigramMap.put('p', 0.0);
		unigramMap.put('q', 0.0);
		unigramMap.put('r', 0.0);
		unigramMap.put('s', 0.0);
		unigramMap.put('t', 0.0);
		unigramMap.put('u', 0.0);
		unigramMap.put('v', 0.0);
		unigramMap.put('w', 0.0);
		unigramMap.put('x', 0.0);
		unigramMap.put('y', 0.0);
		unigramMap.put('z', 0.0);

		// Iterate through the training text
		for (int i = 0; i < inputChars.size() - 1; i++) {
			// Single out each character
			singleLetter = inputChars.get(i);

			// Find that character in the map
			freq = unigramMap.get(singleLetter);

			// Increment its frequency value by 1.0
			unigramMap.put(singleLetter, freq + 1.0);
		}

		return unigramMap;
	}

	/**
	 * This instance method uses a HashMap<String, Double> to store all possible bigram (two-character) combinations 
	 * of the characters in the alphabet (including whitespace) and the number of times each bigram occurs in the training
	 * text. First, all possible alphabetic bigrams are generated and stored in the HashMap. Each bigram is stored as the 
	 * HashMap's key, and for each key, the corresponding value (to represent bigram frequency) is initially set to 1.0. 
	 * The method then iterates through the inputChars ArrayList (containing the training text file). Each time a bigram 
	 * in this ArrayList is found to match a key in the HashMap, the value of that key is incremented by 1.0.
	 * 
	 * @return a HashMap<String, Double> containing bigrams (as key), and the corresponding number of times (as value) a
	 * 		given bigram occurs in the training text
	 */
	public HashMap<String, Double> storeBigramFreq() {

		HashMap<String, Double> bigramMap = new HashMap<String, Double>();
		String firstLetter;
		String secondLetter;
		String bigram;
		Double bigramVal;

		// Store alphabetic characters in an array
		String[] alphabet = {" ","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

		// Generate all possible bigram combinations of the characters in the alphabet array
		for (int i = 0; i < alphabet.length; i++) {
			for (int j = 0; j < alphabet.length; j++) {
				firstLetter = alphabet[i];
				secondLetter = alphabet[j];
				bigram = firstLetter + secondLetter;

				// Store each bigram in the bigramMap and initially set its frequency to 1.0
				bigramMap.put(bigram, 1.0);

				// Remove the double whitespace from the map as it does not occur in the training / input texts
				bigramMap.remove("  ");
			}
		}

		// Iterate through the training text
		for (int i = 0; i < inputChars.size() - 1; i++) {

			// Build each bigram
			firstLetter = Character.toString(inputChars.get(i));
			secondLetter = Character.toString(inputChars.get(i+1));
			bigram = firstLetter + secondLetter;

			// In the bigramMap HashMap, increment the bigram count by 1.0 if it occurs in the training text
			bigramVal = bigramMap.get(new String(bigram));
			if (bigramVal != null) {
				bigramMap.put(bigram, bigramVal + 1.0);
			}
		}

		return bigramMap;
	}

	/**
	 * This instance method uses a HashMap<String, Double> to store the probabilities associated with each bigram contained
	 * in the bigramFreqMap HashMap. The method iterates through the bigramFreqMap map. For each entry, the following is retrieved:
	 * the bigram itself (key), the number of times it occurred in the training text (value) and the number of times the first
	 * character of the bigram occurred in the training text (accessed from the unigramFreqMap).
	 * The probability of the given bigram is then calculated, whereby we address zeroâ€�probability bigrams by utilising a smoothing
	 * method (Laplace's 'Add-One' smoothing) to reduce the weighting given to nonâ€�zero counts, so zeroâ€�counts are given some weighting. 
	 * This is calculated by adding 1 to the bigram frequency, adding 27 (total unique characters) to the frequency of the first 
	 * character, and then dividing the the former by the latter. Then, each bigram is entered into the probsMap HashMap as value, 
	 * and the corresponding value of each bigram is its probability.
	 * 
	 * @return a HashMap<String, Double> containing bigrams (as key), and the corresponding probability (as value) that a
	 * 		given bigram occurs in the training text
	 */
	public HashMap<String, Double> calculateBigramProbs() {

		HashMap<String, Double> probsMap = new HashMap<String, Double>();
		Double bigramProb;
		String bigramkey;
		Double bigramFreq;
		char firstChar;
		Double firstCharFreq;

		for (Map.Entry<String, Double> bigramEntry : bigramFreqMap.entrySet()) {
			bigramProb = 0.0;

			// Get the key of each entry
			bigramkey = (String) bigramEntry.getKey();
			// Get the value (frequency) of each entry
			bigramFreq = (Double) bigramEntry.getValue();

			// Get the first character of each key (whose value is to be the denominator)
			firstChar = bigramkey.charAt(0);

			// Get the number of times the first character occured in the training text
			firstCharFreq = unigramFreqMap.get(firstChar);

			// Below is how we would calculate the bigram probabilities without smoothing
			// bigramProb = bigramFreq / firstCharFreq;

			// Below is how we calculate the bigram probabilities with Laplace's 'Add-One' smoothing
			bigramFreq = bigramFreq + 1;
			firstCharFreq = firstCharFreq + 27;
			bigramProb = bigramFreq / firstCharFreq;

			// Enter the bigram and its probability into the probsMap HashMap
			probsMap.put(bigramkey, bigramProb);
		}

		return probsMap;
	}

	/**
	 * @return the return type of this accessor method is an integer value representing the total number of 
	 * 		characters in the training text
	 */
	public int getCharCount() {
		return charCount; 
	}

	/**
	 * @return the return type of this accessor method is a HashMap<String, Double> containing bigrams (as key), 
	 * 		and the corresponding probability (as value) that a given bigram occurs in the training text
	 */
	public HashMap<String, Double> getProbabilityMap() {
		return bigramProbsMap; 
	}
}
