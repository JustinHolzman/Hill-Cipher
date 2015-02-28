/*
 * 
 * Class: CIS 3360 Security in Computing
 * @author Justin Holzman
 * Due date: 9/26/2013 11:59 PM
 * Programming Assignment One : Create a program to encrypt a file using the Hill Cipher
 *
 */

import java.util.*;
import java.io.*;

public class Holzman_Justin_HillCipher {

	//Constant for line size and Text file char size
	private static final int LINE_MAX = 80;
	private static final int TEXTFILE_MAX = 10000;

	//MAIN
	public static void main(String[] args) {
		
		//Decelerations
		Scanner userInput = new Scanner(System.in);
		String keyFile1 = null;
		int[][] key = new int[500][500];
		int keyRowsCollums = 0;
		int error = 0;
		int length = 0;
		String textMessageInput = "";

		//Loop till they enter a file that is located in the programs directory 
		do {
			System.out.println("Please enter the name of the file storing the key.");		// Ask user for name of Key file

			keyFile1 = userInput.next();		// Place name entered into keyFile1

			File keyFile = new File(keyFile1);		// keyFile = Name of key file entered by the user

			// FILE NAMES FROM USER
			try {
				//Set error to Zero, File was found
				error = 0;
				//@SuppressWarnings("resource")
				Scanner keyFileScanner = new Scanner(keyFile);

				keyRowsCollums = keyFileScanner.nextInt();				//Get the number of Rows/Cols in the matrix

				//Inputs Key into array
				for (int i = 0; i < keyRowsCollums; i++) {
					for (int i2 = 0; i2 < keyRowsCollums; i2++){
						key[i][i2] = keyFileScanner.nextInt();
					} // End for
				} //End for
			} //End try
			
			//File name error
			catch (FileNotFoundException e) {
				System.err.println("Error: that file name was not found.\n");
				error = 1;
			}
		} while (error != 0);

		//Loop till they enter a file that is located in the programs directory 
		do {
			String TextMessageFile;		// Text message file
			error = 0;

			System.out.println("Please enter the name of the file to encrypt.");		// Ask user for name of text file to encrypt

			TextMessageFile = userInput.next();	// Place name of file into TextMessageFileName

			File plaintextFile = new File(TextMessageFile);

			try {
				//@SuppressWarnings("resource")
				Scanner textMessageScanner = new Scanner(plaintextFile);

				// Append TextMessage
				while (textMessageScanner.hasNext() == true){
					textMessageInput += textMessageScanner.next();	
				}// Read next char in file
			} catch (FileNotFoundException e) {
				System.err.println("Error: that file name was not found.\n");
				error = 1;
			}// End Catch
		} while (error != 0);
		
		String cipheredTextFile;	// Cipher

		System.out.println("Please enter the name of the file to store the ciphertext.");	// Get cipher file

		cipheredTextFile = userInput.next();	// Place file name entered into outoutFile

		// Check the text message file for larger than 10,000 characters.
		if (textMessageInput.length() > TEXTFILE_MAX) {
			System.err.println("Error: Text message file is larger than 10,000 characters.");
			System.exit(1); //File larger than 10,000 characters. System.Exit
		}

		char[] newPlaintext = textMessageInput.trim().toLowerCase().toCharArray();	// Change string into a char array and convert toLowerCase.

		char[] plaintext = new char[newPlaintext.length]; //Set size to length of Plaintext

		for (int i = 0; i < newPlaintext.length; i++) {			// Rid of special characters and generate a newPlainText
			if (newPlaintext[i] < 'a' || newPlaintext[i] > 'z')	// Ignore non-letters
				continue;
			else
				plaintext[length++] = newPlaintext[i];
		}// For loop

		char[] cipherText = new char[plaintext.length + 1];	//Send plainText into an Array of characters

		// Multiplying the matrix's
		// Store the letters into the ciphertext array, which has skipped special characters in message.
		for (int letter = 0; letter < length; letter += keyRowsCollums) {
			for (int row = 0; row < keyRowsCollums; row++) {					// Encrypt based on Key matrix
				for (int column = 0; column < keyRowsCollums; column++) {
					if ((letter + column) < length) {						// Check if we need to pad
						cipherText[letter + row] += (char) (key[row][column] * (plaintext[letter + column] - 'a'));
					}// End If
					else {													// Pad on runout
						cipherText[letter + row] += (char) (key[row][column] * ('x' - 'a'));
					}// End else
				}// End for
				cipherText[(letter + row)] = (char) (cipherText[(letter + row)] % 26 + 'a');	// Mod by 26 to get value of char
			}// Nested For
		}// End For

		// Write to Cipher text file
		try {
			// Used to write to Ciphered text file
			FileWriter fstream = new FileWriter(cipheredTextFile);
			BufferedWriter out = new BufferedWriter(fstream);

			// 80 chars per line. So create new line each time length reaches 80.
			for (int x = 0; x < cipherText.length;) {
				for (length = 0; length < LINE_MAX; length++) {
					if (x < cipherText.length)
						out.write(cipherText[x++]);
				}// End for
				out.write("\n");	// New line in file for every 80 char
			}// End For
			out.close(); //Close stream
		}
		// ERROR if file does not work for some reason or another
		catch (IOException ex) {
			System.err.println("ERROR: Check file location");
			System.exit(1); //Exit program
		}
	}// END MAIN
}// Public Class END