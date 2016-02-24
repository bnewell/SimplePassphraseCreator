package com.relabit.spc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

/**
 * Class used to generate random numbers, passwords and passphrases
 * 
 * @author Benjamin S. Newell
 *
 */
public class PasswordGenerator {
	private String currentPassword;
	private String currentPassphrase;
	
	private final static int defaultLowerBound = 12;
	private final static int defaultUpperBound = 31;
	
	private boolean allowSpecialCharacters = true;
	private boolean allowNumbers = true;
	
	/* PRINTABLE ASCI VALUES
	 	48-57   0-9
	 	58-64   :-@
	 	65-90   A-Z
	 	91-96   [-`
	 	97-122  a-z
	 	123-126 {-~     
	*/
	
	// Remember upper bounds are exclusive
	private final int[] boundSequenceForAllPrintableChars = {48, 127};
	private final int[] boundsSequenceToExcludeSpecialChars = {48,58,65,91,97,123};
	private final int[] boundsSequenceToExcludeNumbers = {58, 127};
	private final int[] boundsSequenceToExcludeSpecialCharsAndNumbers = {65,91,97,123};
	
	private Random random;
	
	public PasswordGenerator(){
		random = new Random(System.currentTimeMillis());
		currentPassword = generatePassword(getRandomNumber(defaultLowerBound, defaultUpperBound));
		currentPassphrase = generatePassphrase();
	}
		
	/**
	 * @return
	 * 		The current password. E.g. the last password generated
	 */
	public String getCurrentPassword(){
		return currentPassword;
	}
	
	/**
	 * @return
	 * 		The current passphrase. E.g. the last passphrase generated
	 */
	public String getCurrentPassphrase(){
		return currentPassphrase;
	}
	
	/**
	 * Generates a random password of the specified length. Uses class variables to determine if
	 * to include numbers and special characters
	 * 
	 * Sets the current password when complete
	 * 
	 * @param length
	 * 		The requested length of the password
	 * @return
	 * 		The random generated password
	 */
	public String generatePassword(int length){
		int randNum = 0;
		StringBuilder password = new StringBuilder();
	
		for(int i = 0; i < length; i++){
			if(allowSpecialCharacters){
				if(allowNumbers){
					// All printable characters
					randNum = getRandomNumber(boundSequenceForAllPrintableChars);
				} else {
					// Special characters, No numbers
					randNum = getRandomNumber(boundsSequenceToExcludeNumbers);
				}
			} else {
				if(allowNumbers){
					// No special characters, Numbers
					randNum = getRandomNumber(boundsSequenceToExcludeSpecialChars);
				} else {
					// No special characters, No numbers
					randNum = getRandomNumber(boundsSequenceToExcludeSpecialCharsAndNumbers);
				}
			}
			password.append(Character.toChars(randNum));
		}
		currentPassword = password.toString();
		return currentPassword;
	}
	
	/**
	 * Generates a passphrase composed in this order -> [adjective][noun][verb][preposition][proper noun][3 numbers]
	 * 
	 * Sets the current passphrase when complete
	 * 
	 * @return
	 * 		A string representation of the newly generated passphrase
	 */
	public String generatePassphrase(){
		StringBuilder passphrase = new StringBuilder();
        //
        // Adjective
		String adjective = PartialDictionary.getRandomAdjective();
		// Capitalize first character
		adjective = adjective.substring(0, 1).toUpperCase().concat(adjective.substring(1, adjective.length()));
		//
        // Nouns
		String noun = PartialDictionary.getRandomNoun();
        String noun2 = PartialDictionary.getRandomNoun();
		noun = noun.substring(0, 1).toUpperCase().concat(noun.substring(1, noun.length()));
		noun2 = noun2.substring(0, 1).toUpperCase().concat(noun2.substring(1, noun2.length()));
        //
        // Verb
		String verb = PartialDictionary.getRandomVerb();
		verb = verb.substring(0, 1).toUpperCase().concat(verb.substring(1, verb.length()));
		//
        // Preposition
		String preposition = PartialDictionary.getRandomPreposition();
		preposition = preposition.substring(0, 1).toUpperCase().concat(preposition.substring(1, preposition.length()));
		//
        // Country
		String country = PartialDictionary.getRandomCountry();
        //
		// Build 5-word passphrase
		passphrase.append(adjective);
		passphrase.append(noun);
		passphrase.append(verb);
		passphrase.append(preposition);
        // 50-50 if last word will be noun or country
        if(PartialDictionary.getRandomNumber(100) > 49) {
            passphrase.append(country);
        } else {
            passphrase.append(noun2);
        }

		currentPassphrase = passphrase.toString().replaceAll(" ", "");
		// Randomly decide if we generate a two or three word passphrase
		if(PartialDictionary.getRandomNumber(100) > 33){
            int randNum = PartialDictionary.getRandomNumber(100);
			//two words
			if(randNum < 33){
				adjective = PartialDictionary.getRandomAdjective();
				adjective = adjective.substring(0, 1).toUpperCase().concat(adjective.substring(1, adjective.length()));
				noun = PartialDictionary.getRandomNoun();
				noun = noun.substring(0, 1).toUpperCase().concat(noun.substring(1, noun.length()));
				passphrase = new StringBuilder();
				passphrase.append(adjective);
				passphrase.append(noun);
				//passphrase.append(Integer.toString(getRandomNumber(100, 1000)));
				currentPassphrase = passphrase.toString().replaceAll(" ", "");
			} else if(randNum > 33 && randNum < 66){ // three words
				adjective = PartialDictionary.getRandomAdjective();
				adjective = adjective.substring(0, 1).toUpperCase().concat(adjective.substring(1, adjective.length()));
				verb = PartialDictionary.getRandomVerb();
				verb = verb.substring(0, 1).toUpperCase().concat(verb.substring(1, verb.length()));
				noun = PartialDictionary.getRandomNoun();
				noun = noun.substring(0, 1).toUpperCase().concat(noun.substring(1, noun.length()));
				passphrase = new StringBuilder();
				passphrase.append(adjective);
				passphrase.append(verb);
				passphrase.append(noun);
				//passphrase.append(Integer.toString(getRandomNumber(100, 1000)));
				currentPassphrase = passphrase.toString().replaceAll(" ", "");
			} else { // randNum > 66 = four words
                adjective = PartialDictionary.getRandomAdjective();
                adjective = adjective.substring(0, 1).toUpperCase().concat(adjective.substring(1, adjective.length()));
                preposition = PartialDictionary.getRandomPreposition();
                preposition = preposition.substring(0, 1).toUpperCase().concat(preposition.substring(1, preposition.length()));
                noun = PartialDictionary.getRandomNoun();
                noun = noun.substring(0, 1).toUpperCase().concat(noun.substring(1, noun.length()));
                noun2 = PartialDictionary.getRandomNoun();
                noun2 = noun2.substring(0, 1).toUpperCase().concat(noun2.substring(1, noun2.length()));
                passphrase = new StringBuilder();
                passphrase.append(noun);
                passphrase.append(preposition);
                passphrase.append(adjective);
                passphrase.append(noun2);
                currentPassphrase = passphrase.toString().replaceAll(" ", "");
            }
		}
		return currentPassphrase;
	}
	
	
	public List<String> getPassphraseList(int numPassphrases){
		ArrayList<String> passphrases = new ArrayList<String>();
		
		for(int i = 0; i < numPassphrases; i++){
			passphrases.add(generatePassphrase());
		}
		
		return passphrases;
	}
	
	
	
	/**
	 * Gets a random number between 0 and the specified upper bound
	 * 
	 * @param upperBound
	 * 		Exclusive
	 * @return
	 */	
	public int getRandomNumber(int upperBound){
		return getRandomNumber(0, upperBound);
	}
	
	/**
	 * Gets a random number between the specified bounds
	 * 
	 * @param lowerBound
	 * 		Inclusive
	 * @param upperBound
	 * 		Exclusive
	 * @return
	 * 		The random number
	 */
	public int getRandomNumber(int lowerBound, int upperBound){
		return random.nextInt(upperBound - lowerBound) + lowerBound;
	}
	
	/**
	 * Generates a random number between the specified bounds
	 * 
	 * E.g. Pass in {"0", "11", "15", "21"} and it will generate a random number
	 * between 0-10 and 15-20. Remember uppbounds are exclusive.
	 * 
	 * @param bounds
	 * 		The bounds with which you want to generate a random number
	 * @return
	 * 		The random number generated
	 */
	public int getRandomNumber(int... bounds){
		ArrayList<Integer> theBounds = new ArrayList<Integer>();
		for(int i : bounds){
			theBounds.add(i);
		}
		if(bounds.length % 2 == 1){
			Log.d("getRandomNumber()", "Passed an uneven amount of bounds. Removing last bound.");
			theBounds.remove(theBounds.size()-1);
		}
		// Generate a random number for each of the bounds
		ArrayList<Integer> theNumbers = new ArrayList<Integer>();
		for(int x = 0; x < theBounds.size(); x+=2){
			int lowerBound = theBounds.get(x);
			int upperBound = theBounds.get(x+1);
			theNumbers.add(getRandomNumber(lowerBound, upperBound));
		}
		// Return a random number from the random numbers generated
		return theNumbers.get(getRandomNumber(theNumbers.size()));
	}
	
	/**
	 * Sets whether to allow numbers
	 * 
	 * @param allow
	 * 		True to allow numbers, false to exclude them
	 */
	public void setAllowNumbers(boolean allow){
		allowNumbers = allow;
	}
	
	/**
	 * Sets whether to allow special characters
	 * 
	 * @param allow
	 * 		True to allow special characters, false to exclude them
	 */
	public void setAllowSpecialCharacters(boolean allow){
		allowSpecialCharacters = allow;
	}
}
