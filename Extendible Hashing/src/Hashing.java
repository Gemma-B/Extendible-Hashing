import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * A class that runs a ranked choice vote election
 * 
 * @author Gemma Joy Bertain and Ben Heck
 * @version 11.9.2023
 */
public class Hashing {
	public static void main(String[] args) {
		int bucketSize = 0;
		int keyLength = 0;
		
		if (Integer.parseInt(args[0]) < 1 ) {
			System.out.println("ERROR: block size must be at least 1, setting bucket size to 1");
			args[0] = "1";
		}
		
		if (Integer.parseInt(args[1]) < 0) {
			System.out.println("ERROR: key length must be positive, setting key length to 1");
			args[1] = "1"; 
		}
		Scanner keyboard = new Scanner(System.in);
		
		bucketSize = Integer.parseInt(args[0]);
		keyLength = Integer.parseInt(args[1]);
		
		GlobalDirectory globalDirectory = new GlobalDirectory(1);
		
		System.out.println("what would you like to do?");
		String input = keyboard.nextLine();
		while (!input.equals("q") ) {
			// RUN STUFF
			if (input.charAt(0) == 'i') {

				if (input.substring(2, input.length()).length() == (keyLength)) {
					globalDirectory.insertKey(input.substring(2));
				} else {
					System.out.println("ERROR: bit-string length is longer than the key-length of " + keyLength);
				}
				
				
			} else if (input.charAt(0) =='s') {
				if (input.substring(2, input.length()).length() == (keyLength)) {
					int search = globalDirectory.searchKey(input.substring(2));
					
					if (search == -1) {
						System.out.println(input.substring(2) + " NOT FOUND");
					} else {
						System.out.println(input.substring(2) + " FOUND");
					}
				} else {
					System.out.println("ERROR: bit-string length is longer than the key-length of " + keyLength);
				}
				
				
			} else if (input.charAt(0) == 'p') {
				System.out.println(globalDirectory.toString());
			} else {
				System.out.println("Command not recognized");
			}
			// prompt for next user input
			System.out.println("What would you like to do next");
			input = keyboard.nextLine();
		}
		
		System.out.println("goodbye :)");
	}
}