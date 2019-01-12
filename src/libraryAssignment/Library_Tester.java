package libraryAssignment;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * This class initialises the library's content and allows the user to test all the features (methods) of the library by providing a console menu of options.
 * @author 40156063
 */

public class Library_Tester {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in); //Scanner object to scan user input
		boolean exit = false; //boolean flag used to terminate the program when true

		String choice = ""; //container to hold the user's menu choice input

		Library library = new Library("", "", new ArrayList<Book>(), new ArrayList<User>()); //creates a blank library object initially before the user selects a library and modifies the 
		//library's states.
		
		Logger.getGlobal().info("\n\n************READ ME************* \nDefault user names and passwords: \nID 0: password\tID 1: password123\tID 2: password111\tID 3: passcode"
				+ "\n\nLibrary intro GUI may not be displayed as the front window, if so it can be found behind the eclipse window"
				+ "\n\nEnsure file structure is Library3\\src\\" + "libraryAssignment for .java files and Library3\\resources for .txt files"
				+ "\nThanks, J. 40156063"); //provides trace message 'read me' for assignment marker.
		
		library.selectLibrary(); //calls a method that allows the user to select the library they want to access the content of
		library.loadBooksUsers(); //scans and creates books and users from .txt files, populating the contents of the library
		library.login(); //allows the user to log in.

		library.getIntroScreen(); //provides a gui welcome screen to the user.
		library.getContents(); //prints out the entire book contents of the library.
		library.getMainMenu(); //prints out the library's main menu
		do {
			choice = sc.nextLine(); //user's input of menu choice
			
			//some menu options are only accessible to librarians (isUsrLibrarianStatus() is true)
			if (choice.equalsIgnoreCase("ADD") && (library.isUsrLibrarianStatus())) {
				library.addBookExtend(); //allows the user to add a new book with states
			} else if (choice.equalsIgnoreCase("EDIT") && (library.isUsrLibrarianStatus())) {
				library.editBook(library.validateBookExtend()); //allows the user to edit an existing book's states
			} else if (choice.equalsIgnoreCase("LOAN")) {
				library.loanBook(library.validateBookExtend()); //allows the user to loan an available book out
			} else if (choice.equalsIgnoreCase("RETURN")) {
				library.returnBook(library.validateBookExtend()); //allows the user to return a book they have on loan
			} else if (choice.equalsIgnoreCase("RETURNALL")) {
				library.returnAllBook(); //allows the user to return all the books they have on loan
			} else if (choice.equalsIgnoreCase("DEL") && (library.isUsrLibrarianStatus())) {
				library.deleteBook(library.validateBookExtend()); //allows the user to remove a book from the library
			} else if (choice.equalsIgnoreCase("COVER")) {
				library.getCover(library.validateBookExtend()); //allows the user to see a GUI display of the book's cover
			} else if (choice.equalsIgnoreCase("SHOWB")) {
				library.getContents(); //allows the user to print out the entire book content of the library
			} else if (choice.equalsIgnoreCase("SEARCH")) {
				library.search(); //allows the user to search the library's book content by states
			} else if (choice.equalsIgnoreCase("STATS")) {
				library.sortBooks(); //prints the book content ordered by a book state specified by the user
			} else if (choice.equalsIgnoreCase("MENU")) {
				library.getMainMenu(); //prints the main console menu
			} else if (choice.equalsIgnoreCase("ADDU") && (library.isUsrLibrarianStatus())) {
				library.addUserExtend(); //allows the user to add another user
			} else if (choice.equalsIgnoreCase("SHOWALLU")) {
				library.getUsrDetailsTotal(); //prints every user's details in the library
			} else if (choice.equalsIgnoreCase("SHOWU")) {
				library.getUsrDetails(library.getUsrIndex()); //prints the current user's details
			} else if (choice.equalsIgnoreCase("DUE")) {
				library.getBooksDue(library.getUsrIndex()); //prints the list of books the current user has on loan
			} else if (choice.equalsIgnoreCase("EDITU")) {
				library.editUserDetails(library.getUsrIndex()); //allows the current user to edit their states
			} else if (choice.equalsIgnoreCase("EDITOTHERU") && (library.isUsrLibrarianStatus())) {
				library.editOtherUserDetails(); //allows a user to edit another user's states
			} else if (choice.equalsIgnoreCase("DELETE")) {
				library.deleteUser(); //allows a user to delete their user account
			} else if (choice.equalsIgnoreCase("EXIT")) {
				library.exit(); //prints library's user and book content to .txt files and terminates the program
				exit = true;
			} else { 
				//the user has entered a command not recognised
				System.out.println("Invalid command. Please try again.");
				library.getMainMenu(); //prints main console menu
			}
		} while (exit == false);
		sc.close(); //closes scanner as there is no need for any more user input
	}
}
