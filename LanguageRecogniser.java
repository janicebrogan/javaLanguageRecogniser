package LanguageRecogniserPackage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

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
 * This is the driver class of the Language Recognition project. This class contains methods that create
 * probabilistic language models and recognises the language of a given piece of unseen text in a text file,
 * based on bigram counts of characters (i.e. sequences of two adjacent characters in a String) of the input 
 * text and those of the language models.
 */
public class LanguageRecogniser {

	/**
	 * This is the main method that creates probabilistic models of five different languages by calling the 
	 * LanguageModel constructor to initialise LanguageModel objects. For each object, the file name of a 
	 * 'training' text written in a certain language is passed as an argument to the constructor to build the
	 * model. The main method also presents a console-based user interface that prompts the user to type the 
	 * path name of a text file (not yet seen by the program), whose language they would like the program to 
	 * recognise. Other methods in the class are then called to store the input text bigrams and to calculate the
	 * probability that the input language is that of a given language model. Finally, the user is presented 
	 * with the results through the console. The results constitute the probability that the input file is one 
	 * of the modelled languages, and the language that has the highest probability that it is that which is 
	 * featured in the input file. 
	 *
	 * @throws Exception 
	 * @throws IOException 	 
	 */
	public static void main(String[] args) throws IOException{

		try {
			// Create language models for English, French, Swedish, Basque and Bahasa Indonesia
			LanguageModel english = new LanguageModel("englishTraining.txt");
			HashMap<String, Double> englishProbsMap = english.getProbabilityMap();

			LanguageModel french = new LanguageModel("frenchTraining.txt");
			HashMap<String, Double> frenchProbsMap = french.getProbabilityMap();

			LanguageModel swedish = new LanguageModel("swedishTraining.txt");
			HashMap<String, Double> swedishProbsMap = swedish.getProbabilityMap();

			LanguageModel basque = new LanguageModel("basqueTraining.txt");
			HashMap<String, Double> basqueProbsMap = basque.getProbabilityMap();

			LanguageModel bahasaIndo = new LanguageModel("bahasaIndonesiaTraining.txt");
			HashMap<String, Double> bahasaIndoProbsMap = bahasaIndo.getProbabilityMap();

			// Introduce the program to the user and prompt the user to input a text file
			Scanner scan = new Scanner(System.in);
			System.out.println("This program will determine whether a given text is English, French, Swedish, Basque or Bahasa Indonesia.");
			System.out.println("\n");
			System.out.println("Please type the file name of the text you would like the program to analyse: ");
			System.out.println("\n");
			String userChoice = scan.nextLine();
			System.out.println("\n");

			// Call the readInput method to store the bigrams of the input text file in an ArrayList<String>
			ArrayList<String> inputBigrams = readInput(userChoice);
			scan.close();

			/* For each language, call the calculateInputProbability method to calculate the probability that the input 
			 * language is that of a given language model. Passed as arguments are the input text bigrams and the bigram
			 * probability maps of each LanguageModel object. Store and print each probability result as a double value. */
			double probabilityEnglish = calculateInputProbability(inputBigrams, englishProbsMap);
			System.out.println("Overall probability that this text is English = ");
			System.out.printf("%.14f", probabilityEnglish);
			System.out.println("\n");

			double probabilityFrench = calculateInputProbability(inputBigrams, frenchProbsMap);
			System.out.println("Overall probability that this text is French = ");
			System.out.printf("%.14f", probabilityFrench);
			System.out.println("\n");

			double probabilitySwedish = calculateInputProbability(inputBigrams, swedishProbsMap);
			System.out.println("Overall probability that this text is Swedish = ");
			System.out.printf("%.14f", probabilitySwedish);
			System.out.println("\n");

			double probabilityBasque = calculateInputProbability(inputBigrams, basqueProbsMap);
			System.out.println("Overall probability that this text is Basque = ");
			System.out.printf("%.14f", probabilityBasque);
			System.out.println("\n");

			double probabilityBahasaIndo = calculateInputProbability(inputBigrams, bahasaIndoProbsMap);
			System.out.println("Overall probability that this text is Bahasa Indonesia = ");
			System.out.printf("%.14f", probabilityBahasaIndo);
			System.out.println("\n");

			System.out.println("\n");
			String result = "";
			double greaterProb = 0.0;

			// Determine which language has the highest probability
			if (probabilityEnglish > probabilityFrench) {
				greaterProb = probabilityEnglish;
				result = "English";
			} else if (probabilityFrench > probabilityEnglish) {
				greaterProb = probabilityFrench;
				result = "French";
			} 

			if (greaterProb < probabilitySwedish) {
				greaterProb = probabilitySwedish;
				result = "Swedish";
			}

			if (greaterProb < probabilityBasque) {
				greaterProb = probabilityBasque;
				result = "Basque";
			} 

			if (greaterProb < probabilityBahasaIndo) {
				greaterProb = probabilityBahasaIndo;
				result = "Bahasa Indonesia";
			} 

			System.out.println("The program determined that the language of the input file is most likely to be " + result + "."); 

		}
		catch (IOException e) {
			System.out.println("IOException was thrown.");
			System.out.println("Exception message: " + e.getMessage());
		}
		catch (Exception e) {
			System.out.println("Exception was thrown.");
			System.out.println("Exception message: " + e.getMessage());
		}
	}

	/**
	 * This method reads in all characters that are contained in the given input text file and first stores each character
	 * in an ArrayList<Character>. Then, the method iterates through the ArrayList<Character>, isolates each bigram and 
	 * stores the bigram in an ArrayList<String>.
	 *  	  
	 * @param inputFilename the String that contains the file path name of the unseen input file to be read in
	 * @return 			 an ArrayList<String> containing the bigrams found in the input text file
	 * @throws IOException 
	 * @throws Exception 
	 */
	public static ArrayList<String> readInput(String inputFilename) throws IOException {

		ArrayList<String> inputBigramList = new ArrayList<String>();
		ArrayList<Character> inputCharList = new ArrayList<Character>();
		char[] charArray;
		String firstLetter;
		String secondLetter;
		String bigram;

		Scanner scan = null;

		try {

			String inputPath = System.getProperty("user.dir") + File.separator + inputFilename;

			scan = new Scanner(new File(inputPath));

			// Store each character in an ArrayList<Character>
			while (scan.hasNext()) {
				charArray = scan.nextLine().toLowerCase().toCharArray();
				for (Character c : charArray) {
					inputCharList.add(c);
				}
			}

			// Isolate and store each bigram in an ArrayList<String>
			for (int i = 0; i < inputCharList.size() - 1; i++) {
				firstLetter = Character.toString(inputCharList.get(i));
				secondLetter = Character.toString(inputCharList.get(i+1));
				bigram = firstLetter + secondLetter;
				//System.out.println(bigram);
				inputBigramList.add(bigram);
			}
			scan.close();
		} 
		catch (IOException e) {
			System.out.println("IOException was thrown.");
			System.out.println("Exception message: " + e.getMessage());
		}
		catch (Exception e) {
			System.out.println("Exception was thrown.");
			System.out.println("Exception message: " + e.getMessage());
		}
		return inputBigramList;
	}

	/**
	 * This method iterates through the ArrayList<String> containing the bigrams found in the input text file. For each input bigram
	 * that is found to match a bigram in the probability HashMap of a given LanguageModel object, the model's bigram probability is 
	 * retrieved. The log of each probability value is summed accumulatively for each matching bigram. 
	 * 
	 * @param inputBigramList the ArrayList<String> containing the bigrams found in the input text file
	 * @param langProbsMap    a LanguageModel instance variable of type HashMap<String, Double> containing bigrams (as key), 
	 * 					   and the corresponding probability (as value) that a given bigram occured in the training text of
	 * 					   the language of the given LanguageModel object
	 * @return 			   the value of type double that represents the probability that the input language is that of a given
	 * 					   language model
	 */
	public static double calculateInputProbability(ArrayList<String> inputBigramList, HashMap<String, Double> langProbsMap) {

		double probability = 0.0;
		String inputBigram;
		double bigramFreq;

		// Iterate through the input text bigrams
		for (int i = 0; i < inputBigramList.size(); i++) {
			inputBigram = inputBigramList.get(i);

			// If the bigram matches a bigram in the LanguageModel map
			if (langProbsMap.containsKey(inputBigram)) {

				// Get the probability of the matching bigram
				bigramFreq = langProbsMap.get(inputBigram);

				// Accumulatively sum the log of each probability value
				probability = probability + Math.log(bigramFreq);
			}
		}
		return probability;
	}
}
