package libraryAssignment;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * This class models a library and defines the library's functions that manipulate or display the library's book/user content.
 * @author 40156063
 */
public class Library {
	private Scanner sc = new Scanner(System.in); //new scanner object for user's input

	//instance variables used to construct the library object.
	private String libName;
	private String libAddress; 
	private ArrayList<Book> listofBooks = new ArrayList<Book>();
	private ArrayList<User> listofUsers = new ArrayList<User>();

	// containers to hold two types of user input (String or integer).
	private String str_userChoice = "";
	private int int_userChoice = 0;

	boolean validInput = false; // boolean flag used to validate user input through a while loop
	//used in multiple methods for validation.

	// containers to hold the file directories of the book and user content .txt files of the library
	// to scan from when first executing the program and print to saving the library's content.
	private String bookDirect = "";
	private String usrDirect = "";

	private int usrID = -1; // current user's unique ID -> during log in it is the user's input of user ID
	private int usrIndex = -1; // index of the current user in the arrayList listofUsers
	private boolean usrLibrarianStatus = false; // true if user is a librarian, false otherwise. 
	//Boolean flag for validating whether the user can access some methods manipulating the list of books or users (adding, editing)

	// the ID of the book/user with the largest ID. Given to a new object and
	// increments each time a new object is created or the ID state of an object has been edited to the largest value.
	private int maxID = -1;
	private int usrMaxID = -1;
	
	private ArrayList<Book> listofBooksSorted; //container for a copy of ArrayList listofBooks which is then sorted by 
	//a user specified book object state.

	/**
	 * This method constructs a library object with all its states.
	 * @param libName - library name
	 * @param libAddress - library address
	 * @param listofBooks - array list containing all of the library's book content listed as object references
	 * @param listofUsers - library's user content listed as object references
	 */
	public Library(String libName, String libAddress, ArrayList<Book> listofBooks, ArrayList<User> listofUsers) {
		this.setLibName(libName);
		this.setLibAddress(libAddress);
		this.listofBooks = listofBooks;
		this.listofUsers = listofUsers;
	}

	/**
	 * This method allows the user to select which library they want to access the content of.
	 */
	public void selectLibrary() {
		while (validInput == false) {
			System.out.println(
					"Which Library do you wish to access: \n(MCCLAY) McClay Library\n(MED) QUB Medical & HSC\n(LINEN) Linen Hall Library");
			str_userChoice = sc.nextLine(); //user input to choose what library they want to access the contents of
			if (str_userChoice.equalsIgnoreCase("MED")) {
				setLibName("Queen's Medical and HSC Library"); // sets the library name corresponding to the user's choice of library
				setLibAddress("Queen's University Building Mulhouse, Boucher Centre, Mulhouse Rd, Belfast BT12 6DP"); // sets the library address
				bookDirect = "resources/MedBooks.txt"; // sets the file directory to the .txt file containing the library's book content
				usrDirect = "resources/MedUsers.txt"; // .txt file containing the library's user content
				validInput = true; //library selection finished
			} else if (str_userChoice.equalsIgnoreCase("LINEN")) {
				setLibName("Linen Hall Library");
				setLibAddress("17 Donegall Square N, Belfast, Co. Antrim BT1 5GB");
				bookDirect = "resources/LinenBooks.txt";
				usrDirect = "resources/LinenUsers.txt";
				validInput = true;
			} else if (str_userChoice.equalsIgnoreCase("MCCLAY")) {
				setLibName("McClay Library");
				setLibAddress("College Park Ave, Belfast BT7 1LQ");
				bookDirect = "resources/McClayBooks.txt";
				usrDirect = "resources/McClayUsers.txt";
				validInput = true;
			}
		}
		validInput = false;
	}

	/**
	 * This method forces the user to enter their user ID and password before accessing the library
	 * Validates both the user's ID and password input are states of a user object
	 * in the library's list of users.
	 */
	public void login() {
		int loginAttempts = 3; // number of log in attempts the user has (default 3)
		String usrPass = ""; // contains the user's password to be checked

		while (validInput == false) {
			if (loginAttempts == 0) { //validates whether the user has depleted their log in attempts
				System.out.println("You have been locked out of the library.");
				System.exit(0); // terminates the program, locking the user out of the library.
			}
			System.out.println("What is your user ID?");
			if (sc.hasNextInt()) { //validates whether the user has entered an integer
				usrID = sc.nextInt(); // input for entering user id
				sc.nextLine();
			}
			System.out.println("What is your password?");
			usrPass = sc.nextLine(); // user input -> entering password as a String
			for (int i = 0; i < getListofUsers().size(); i++) { // for loop references every user object in the library's list of users.
				//validates whether at index (i) the user object's user ID and password state are equivalent to both of the user's input.
				if ((getListofUsers().get(i).getUsrID() == usrID) && getListofUsers().get(i).getUsrPassword().equals(usrPass)) { 
					//user has entered a valid user ID and password - a user object has both these states.
					System.out.println("Login accepted!");
					validInput = true; 
				}
			}
			if (validInput == false) { //validates whether a user object does not exist with the user input user ID and password as states.
				//user has entered input an invalid password and/or user ID -> no user object exists in the library's list of users with them as states.
				loginAttempts--; //user depletes a log in attempt
				System.out.println("Incorrect login details. Attempts remaining: " + loginAttempts);
			}
		}
		usrIndex = validateUser(getUsrID()); //retrieves the index of the current user in the ArrayList listofUsers by 
		//calling the validateUser method which matches the user's ID to the index of the user object in the list of users.
		usrLibrarianStatus = getListofUsers().get(usrIndex).getLibrarianStatus(); //librarian boolean flag assigned to the current user
		//true (librarian), false (non-librarian)
		validInput = false;
	}

	/**
	 * This method provides the user with a GUI introduction to the library the user selected, displaying a short description of its user and book contents
	 * alongside an image of the library.
	 */
	public void getIntroScreen(){
		String imageURL = ""; //container to hold a String URL of an image of the library to be used to created an URL object.

		//sets the String image URL depending on the user's selection of library
		if (getLibName().equals("McClay Library")) { 
			imageURL = "http://www.qub.ac.uk/home/StudyatQueens/UndergraduateStudents/StudyingatQueens/LibraryFacilities/Imagesource,167588,en.jpg";
		} else if (getLibName().equals("Queen's Medical and HSC Library")) {
			imageURL = "http://www.qub.ac.uk/schools/SchoolofNursingandMidwifery/StudyattheSchool/FacilitiesandResources/MedicalLibrary/Image,456386,en.jpg";
		} else {
			imageURL = "http://cain.ulst.ac.uk/images/photos/belfast/lhl.jpg";
		}
			try {
				URL imageIntro = new URL(imageURL); //creates a new URL object from the String image URL.
				//GUI introduction displaying the library's states and image. 
				JOptionPane.showMessageDialog(null,
						"Welcome, " + getListofUsers().get(usrIndex).getUsrName() + ".\n\n"  
				+ "Address: " + getLibAddress() + "\nTotal number of unique books available: " + getBooksAvailable() 
				+ "\nTotal number of users: " + getListofUsers().size(),
						getLibName(), JOptionPane.PLAIN_MESSAGE,
						new ImageIcon(imageIntro));
			} catch (MalformedURLException e) { 
				//the String URL is not a formatted URL - cannot create an URL object.
				e.printStackTrace();
			}
	}
	/**
	 * This method scans input from .txt files and creates Book and User objects with corresponding states, 
	 * adding them to the list of Books/Users ArrayList -> populates library's content.
	 */
	public void loadBooksUsers() {
		//creates two file objects using the .txt file directory of the library's books and users (String) as arguments. Used to scan from for input.
		File BookFile = new File(getBookDirect());
		File UserFile = new File(getUsrDirect());
		try {
			//scanners to scan for input in both files.
			Scanner sc2 = new Scanner(BookFile);
			Scanner sc3 = new Scanner(UserFile);
			
			//containers to hold scanned book input from book .txt file.
			int bookID = 0;
			String title = "";
			String author = "";
			String genre = "";
			BookGenres enumGenre;
			int numTimesLoaned = 0;
			String loanStatus = "";
			int quantity = 0;
			int quantityOnLoan = 0;
			String coverURL = "";
			
			//containers to hold scanned user input from user .txt file.
			String usrName = "";
			int usrID = 0;
			String usrPass = "";
			String status = "";
			ArrayList<Integer> usrBooksLoaned = new ArrayList<Integer>();

			while (validInput == false) {
				if (sc2.hasNextLine()) { //validates whether there is more in the file to scan (whether scanner has reached the limit of .txt file)
					//scan each line of the file and assign the input to the relevant state container.
					bookID = Integer.parseInt(sc2.nextLine());
					title = sc2.nextLine();
					author = sc2.nextLine();
					genre = sc2.nextLine();
					enumGenre = validateGenre(genre);
					numTimesLoaned = Integer.parseInt(sc2.nextLine());
					loanStatus = sc2.nextLine();
					quantity = Integer.parseInt(sc2.nextLine());
					quantityOnLoan = Integer.parseInt(sc2.nextLine());
					coverURL = sc2.nextLine();

					//validates whether the scanned bookID is larger than the max bookID, if so max ID is assigned the bookID. - largest bookID
					if (bookID > getMaxID()) {
						//scanned book ID is the largest
						setMaxID(bookID); //assigns scanned book ID to the max ID.
					}
					
					//validates whether coverURL is null instead of "null" (empty) as scanner cannot parse null as null.
					if (coverURL.equals("null")) {
						coverURL = null; //sets cover URL to empty.
					}
					
					//validates the loan status of the book and sets it to true if scanned input is "n" and false else,
					//using this to create the book object using the addBook method.
					if (loanStatus.equals("n")) {
						//book is not available
						addBook(bookID, title, author, enumGenre, numTimesLoaned, false, quantity, quantityOnLoan,
								coverURL);
					} else {
						//book is available  - 'y'
						addBook(bookID, title, author, enumGenre, numTimesLoaned, true, quantity, quantityOnLoan, coverURL);
					}
				} else { 
					//limit of the books .txt file reached (no more lines left to scan)
					validInput = true;
				}
			}
			sc2.close(); //closes scanner object used to scan input from book content .txt file.
			
			validInput = false;
			while (validInput == false) {
				if (sc3.hasNextLine()) {
					usrName = sc3.nextLine();
					usrID = sc3.nextInt();
					sc3.nextLine();
					usrPass = sc3.nextLine();
					status = sc3.nextLine();

					//loops scanning the users .txt file, adding each scanned integer(individual book ID) 
					//to the list of books the user has currently loaned out
					//Terminates when the next scanned input is not an integer (start of the next user's usrName or empty (limit of file reached)
					while (sc3.hasNextInt()) {
						usrBooksLoaned.add(sc3.nextInt()); 
						sc3.nextLine();
					}

					if (usrID > getUsrMaxID()) {
						setUsrMaxID(usrID);
					}

					if (status.equals("true")) { //validates the status of the user and sets it to true if scanned input is  "true" (user is a librarian)
						                         // or false else (non-librarian), using this to create a user object using the addUser method.
						//user is a librarian
						addUser(usrName, usrID, usrPass, true, usrBooksLoaned); //creates user object
					} else {
						//non-librarian
						addUser(usrName, usrID, usrPass, false, usrBooksLoaned);
					}
					usrBooksLoaned.clear(); //clears booksLoaned ArrayList of all its elements to use for another user being scanned with
					//a new set of books loaned out
				} else {
					validInput = true; //limit of the users .txt file reached
				}
			}
			sc3.close();
			validInput = false;
		} catch (FileNotFoundException e) { 
			//.txt file could not found in the directory specified to create a file - the library's content cannot be scanned.
			System.out.println("Cannot load library content, exiting library...");
			System.exit(0); //terminates the program
		}
	}
	/**
	 * This method creates a new book object and catalogues it in the library by adding it to the list of books ArrayList (library's book content)
	 * @param bookID - unique id of the book
	 * @param title - the title of the book
	 * @param author - the author of the book
	 * @param genre - the genre of the book
	 * @param numTimesLoaned - number of times the book has been loaned.
	 * @param loanStatus - the availability of the book (true if quantity is greater than the number of books on loan, false otherwise)
	 * @param quantity - quantity of the book
	 * @param quantityOnLoan - how many books are out on loan
	 * @param coverURL - the url of the cover image ending in .jpg or null (empty)
	 */
	public void addBook(int bookID, String title, String author, BookGenres genre, int numTimesLoaned,
			boolean loanStatus, int quantity, int quantityOnLoan, String coverURL) {
		Book newBook = new Book(bookID, title, author, genre, numTimesLoaned, loanStatus, quantity, quantityOnLoan,
				coverURL); //creates a new book object with states
		getListofBooks().add(newBook); //catalogues book, adding it to the list of books ArrayList.
	}
	/**
	 * This method asks the user for the states of the new book 
	 * and calls the addBook method to create a new book object with these user input states.
	 */
	public void addBookExtend() {
		//containers to hold the user's input (the new book's states)
		String title = "";
		String author = "";
		BookGenres genre;
		int quantity = 0;
		String coverURL = "";

		System.out.println("Please enter the title:  ");
		title = sc.nextLine(); //asks the user for the title of the book
		System.out.println("Please enter the author: ");
		author = sc.nextLine(); //asks the user for the author of the book
		genre = validateGenreExtend(); //calls method which returns a BookGenre value through the user's input in the method.
		while (validInput == false) {
			System.out.println("Please enter the quantity:");
			if (sc.hasNextInt()) { //validates whether the user has entered an integer.
				quantity = sc.nextInt(); //asks the user for the quantity of the new book
				if (quantity > 0) { //validates whether the quantity is greater than 0 (negative number of or no books does not make sense when adding a new book)
					validInput = true; //user input quantity is valid
				} else {
					//the user has entered a quantity less than or equal to zero
					System.out.println("The quantity must be a positive integer greater than 0. Please try again.");
				}
			}
			sc.nextLine();
		}
		validInput = false;
		setMaxID(getMaxID() + 1); //increments the max book ID - the book ID of the new book to be assigned the incremented max ID
		
		while (validInput == false) {
			System.out.println("(Optional, leave blank if none) Please enter cover URL of book: ");
			coverURL = sc.nextLine(); //asking user for the cover URL of the book
			if (coverURL.equals("")) { //validates whether the user has entered no cover url ("" empty String)
				//user did not enter a cover URL
				coverURL = null; //sets cover URL to empty (null)
				validInput = true;
			} else {
				//the user has entered a String of one or more characters
				coverURL = validateURL(coverURL); //calls the validateURL method to validate if the user's input is a formatted URL, 
				//returns null if not. The cover URL is assigned this return.
				validInput = true;
			}
		}
		validInput = false;
		
		addBook(getMaxID(), title, author, genre, 0, true, quantity, 0, coverURL); 
		//calls the addBook method to create the new book with the user's states.
		//the book is by default available (loan status = true), not on loan (quantity on loan = 0) and has not been loaned before (quantity loaned = 0)
		System.out.println("New book's ID is:" + getMaxID()); //provides the user with the new book's ID to be able to reference it with other menu options
		getMainMenu();
	}
	/**
	 * This method allows the user to edit a book's title, author, genre, book ID, number of times the book has been loaned out
	 * , availability of the book, quantity of book in the library, quantity of book on loan and the cover URL of the book
	 * i.e every book object state
	 * @param index - index of the user specified book in the list of books ArrayList.
	 */
	public void editBook(int index) {
		boolean repeatID = false; //flag if there is a repeated book ID
		boolean editingState = true; //flag to indicate the user is editing a book, assigned false when the user wants to stop editing.
		
		while (editingState == true) { //validates the user has not stopped editing (editingState = true)
			//console menu printing editing book states options.
			System.out.println("What property of " + listofBooks.get(index).getTitle() + " do you wish to edit?"
					 + "\n(TITLE)Title\t\t(NUMLOAN)Number of times loaned out\n"
					 + "(AUTHOR)Author\t\t(ONLOAN)Availability Status \n(BOOKID)Book ID\t\t"
					 + "(QUANTLOAN) Quantity of books on loan\n(GENRE) Genre\t\t(COVER) Cover URL\n"
					 + "(QUANTBOOK) Quantity of Book\t(EXIT)Exit editing");
				str_userChoice = sc.nextLine(); //user's input choice of which book state they want to edit.
				
				if (str_userChoice.equalsIgnoreCase("TITLE")) {
					System.out.println("Please enter a new title: ");
					str_userChoice = sc.nextLine();//asking the user for the new title of the book
					listofBooks.get(index).setTitle(str_userChoice); //sets title
					System.out.println("Title successfully changed.");
				} else if (str_userChoice.equalsIgnoreCase("AUTHOR")) { 
					System.out.println("Please enter a new author: ");
					str_userChoice = sc.nextLine();
					listofBooks.get(index).setAuthor(str_userChoice); //sets author
					System.out.println("Author successfully changed.");
				} else if (str_userChoice.equalsIgnoreCase("GENRE")) {
					BookGenres genre = validateGenreExtend(); //calls a method which returns a BookGenre value (the new book's genre state) through the user's input
					listofBooks.get(index).setGenre(genre); //sets genre
					System.out.println("Genre successfully changed.");
				} else if (str_userChoice.equalsIgnoreCase("BOOKID")) {
					if (listofBooks.get(index).getQuantityOnLoan() > 0) { //validates whether the book is on loan.
						//the book ID is in some users' list of books loaned
						//the book ID cannot be different as that would mean the user cannot return it with the new book ID.
						System.out.println("Book ID cannot be edited while it is on loan "); 
					} else {  
						//book is not on loan
						while (validInput == false) {
							repeatID = false; //flag resets with each iteration
							System.out.println("Please enter the updated Book ID.");
							if (sc.hasNextInt()) { //validates whether user has entered an integer
								int_userChoice = sc.nextInt(); //asks user for new book ID
								for (int i = 0; i < listofBooks.size(); i++) { //accesses every book object element in the listofBooks ArrayList
									if (listofBooks.get(i).getBookID() == int_userChoice) { //validates whether another book in the library's book content has the same ID as the user input
										repeatID = true; //flag indicating there exists a book with the same bookID
										System.out.println("A book already shares this ID. Please try again.");
									}
								}
								if (repeatID == false) { //if there does not exist a book with the same bookID
									listofBooks.get(index).setBookID(int_userChoice); //sets the book object book ID to the user's input
									System.out.println("Book ID successfully changed.");
									validInput = true; //editing of book ID finished
								}
						}
							sc.nextLine();
						if (int_userChoice > maxID) { //validates whether the user's newly assigned book ID is larger than the max ID.
							//Future books added will have book IDs auto incremented from this value.
							setMaxID(int_userChoice); //assigns the maxID to the value of the user's input.
						}
					}
					}
				} else if (str_userChoice.equalsIgnoreCase("NUMLOAN")) {
					while (validInput == false) {
						System.out.println("Please enter updated number of times book has been out on loan: ");
						if (sc.hasNextInt()) {
							int_userChoice = sc.nextInt(); //asks users for a new number of times loaned
							if (int_userChoice >= 0) { //validates whether the number of times the book is not negative - does not make logical sense
								//number of times loaned is valid
								validInput = true; //editing of number of times loaned finished
							} else { 
								//user input number of times loaned is invalid.
								System.out.println("The number of times the book has been out on loan must be equal to or greater than 0.");
							}
						}
						sc.nextLine();
					}
					listofBooks.get(index).setNumofLoan(int_userChoice); //sets the book object number of loans to the user's validated input
					System.out.println("Number of times the book has been loaned out successfully changed.");
				} else if (str_userChoice.equalsIgnoreCase("ONLOAN")) {
					while (validInput == false) {
						System.out.println("Please enter the changed availability:\ny for available\nn for not available");
						str_userChoice = sc.nextLine(); //user input for a change in the book's availability state
						
						//user's input is validated and if the user decides to change the status to change the status to available
						//(true) there must be books available to loan (quantity does not equal quantity on loan)
						if (str_userChoice.equalsIgnoreCase("y")) {
							if (listofBooks.get(index).getQuantityOnLoan() != listofBooks.get(index).getQuantity()) {
								listofBooks.get(index).setOnLoan(true); //sets book object's availability status to true.
								System.out.println("Availability successfully changed.");	
								validInput = true; //editing of availability status finished
							 }
							else {
								//book object's quantity state is equal to its quantity on loan status
								System.out.println("You cannot change a book's status to available if there is no books available to loan.");
							}
						}
						else if (str_userChoice.equalsIgnoreCase("n")) { //user decides to change book's availability to 'not available' (false)
							listofBooks.get(index).setOnLoan(false); //sets book object's loan status to false.
							validInput = true; //editing of availability status finished.
							System.out.println("Availability successfully changed.");
						}
						else {
							//user did not enter "y" or "n", cannot change the book's availability with their input
							System.out.println("Invalid command You did not enter 'y' or 'n'."); 
						}
					}
				} else if (str_userChoice.equalsIgnoreCase("QUANTBOOK")) {
					while (validInput == false) {
						System.out.println("Please enter an updated quantity");
						if (sc.hasNextInt()) { //validates whether the user has entered an integer
							int_userChoice = sc.nextInt(); //user input for the new quantity of books
							//validates whether the user's input is greater than the quantity on loan
							//there must be a quantity of books greater than or equal to the quantity of books on loan
							if (int_userChoice > listofBooks.get(index).getQuantityOnLoan()) { 
								listofBooks.get(index).setQuantity(int_userChoice); //sets quantity to the user's input
								System.out.println("Quantity successfully changed.");
								validInput = true; //editing of book quantity finished
							} else {
								//quantity of book on loan is greater than the user's input new quantity
								System.out.println(	"Quantity of book has to be greater than the quantity on loan. Please try again.");
							}
						}
						sc.nextLine();
					}
				} else if (str_userChoice.equalsIgnoreCase("QUANTLOAN")) {
					while (validInput == false) {
						if (listofBooks.get(index).isOnLoan() == false) { //validates the availability status of the book
							//book is not available, quantity on loan cannot be changed.
							System.out.println("Sorry, the book is not available. You cannot change the number of books on loan.");
							validInput = true; //editing of quantity on loan state finished.
						} else { 
						System.out.println("Please enter an updated quantity on loan: ");
						if (sc.hasNextInt()) { //validates the user has entered an integer
							int_userChoice = sc.nextInt(); //user input for the new quantity of books on loan
							
							//validates that the quantity is greater than or equal to the quantities on loan
							//does not make sense for there to be more books on loan than books owned by the library
							//also validates that the quantity on loan is greater than the current value
							//ensures books currently on loan are not lost.
							if (listofBooks.get(index).getQuantity() >= int_userChoice
									&& int_userChoice > listofBooks.get(index).getQuantityOnLoan()) {
								int currentQuantity = listofBooks.get(index).getQuantityOnLoan(); //contains the quantity on loan before the book's quantity on loan is added to
								//loops for the difference of starting books on loan and user's new books on loan input -> loans the difference
								for (int i = 0; i < int_userChoice - currentQuantity; i++) {
									loanBook(index); //loans book out
								}
								System.out.println("Quantity on loan successfully changed.");
								validInput = true; //editing of quantity on loan state finished.
							}
						}
						sc.nextLine();
						}
					}
				} else if (str_userChoice.equalsIgnoreCase("COVER")) {
					System.out.println("Please enter a cover URL :");
					str_userChoice = sc.nextLine(); //user input of the new book's cover url

					if (str_userChoice == "") { //validates whether the user has not entered a String, indicating they do not want a cover URL assigned to the book
						str_userChoice = null; //sets user input to empty (null)
					}
					listofBooks.get(index).setCoverURL(validateURL(str_userChoice)); //sets the cover url state to the user's input
					System.out.println("Cover URL successfully changed.");
				} else if (str_userChoice.equalsIgnoreCase("EXIT")) {
					//user has finished editing
					System.out.println("Returning to main menu...");
					editingState = false; //flag assigned false - exits while statement and editBook() method
				}
				else {
					//user's input of book state to edit not recognised
					System.out.println("InvaLid command. Please try again.");
				}
				validInput = false;
		}
		getMainMenu();
	}
	/**
	 * This method allows the user to delete a book by removing it from the ArrayList listofBooks. 
	 * @param index - the index of the book object the user wants to delete in the ArrayList listofBooks
	 */
	public void deleteBook(int index) {
		if (listofBooks.get(index).getQuantityOnLoan() > 0) { //validates that the book is not on loan to user(s) - prevents ghost books in a user's list of books due.
			System.out.println("Book cannot be deleted while it is on loan "); 
		} else if (listofBooks.size() == 1) { //validates that there is at least one book in the library. You cannot have library with no content.
			System.out.println("There must be at least one book in the library.");
		} else { 
			//the book is not on loan and the library has at least one more book
			System.out.println(
					"Are you sure you wish to delete: " + listofBooks.get(index).getTitle() + "? Press y to confirm.");
			str_userChoice = sc.nextLine(); //user input to confirm they want to delete the book
			if (str_userChoice.equalsIgnoreCase("y")) {
				//user confirmation "y" they want to delete the book
				listofBooks.remove(index); //removes book from the ArrayList, the book is no longer part of the library's book content.
				System.out.println("You have successfully deleted the book.");
			} else { 
				//user does not enter "y" -> decided not to remove the book.
				System.out.println("You have decided not to delete the book.");
			}
		}
		getMainMenu();
	}
	/**
	 * This method allows the user to loan out a book and adds the book to the list of books 
	 * @param index - the index of the book object the user wants to loan in the ArrayList listofBooks the user has specified by their bookID
	 */
	public void loanBook(int index) {
		//validates the book is available for loan
		if (listofBooks.get(index).isOnLoan() == true) {
			//validates whether a non-librarian has exceeded their 3 book loan limit
			if (!listofUsers.get(usrIndex).getLibrarianStatus() && listofUsers.get(usrIndex).getBooksLoaned().size() == 3) {
				//non-librarian already has three books on loan - cannot loan another book until they return one.
				System.out.println("You cannot loan out this book as you have exceeded your loan limit (3 books).");
			} else {
				if (listofBooks.get(index).getQuantity() == listofBooks.get(index).getQuantityOnLoan() + 1) { //validates the book is one short of all its copies on loan (quantity is equal to quantity on loan), 
					//once the new book is loaned out the book should not be available to loan. (availability status false)
					listofBooks.get(index).setOnLoan(false); //sets book to not available (false)
				}
					listofBooks.get(index).setNumofLoan(listofBooks.get(index).getNumofLoan() + 1); //increments the number of loans
					listofBooks.get(index).setQuantityOnLoan(listofBooks.get(index).getQuantityOnLoan() + 1); //increments the quantity on loan
					System.out.println(listofBooks.get(index).getTitle() + " has been loaned out.");
					listofUsers.get(usrIndex).getBooksLoaned().add(listofBooks.get(index).getBookID()); //adds the book loaned's ID to the users books loaned list
			}
			} else {
			//the book is not available for loan
			System.out.println(listofBooks.get(index).getTitle() + " is not available.");
		}
	}
	
	/**
	 * This method allows the user to return a book in their book loaned list.
	 * @param index -  the index of the book object the user wants to return in the ArrayList listofBooks the user has specified by their bookID
	 */
	public void returnBook(int index) {
		//validates the user has the book on loan (in their list of books loaned)
		if (listofUsers.get(usrIndex).getBooksLoaned().contains(listofBooks.get(index).getBookID())) {
			//validates that if the book is not available (quantity = quantity on loan) then as a book is being returned the availability status should be available (true)
			if (listofBooks.get(index).isOnLoan() == false) {
				listofBooks.get(index).setOnLoan(true); //book now available
			}
			listofBooks.get(index).setQuantityOnLoan(listofBooks.get(index).getQuantityOnLoan() - 1); //decrements the number of books on loan
			System.out.println(
					listofBooks.get(index).getTitle() + " has been returned.\n" + listofBooks.get(index).getBookID());
			index = listofUsers.get(usrIndex).getBooksLoaned().indexOf(listofBooks.get(index).getBookID());
			listofUsers.get(usrIndex).getBooksLoaned().remove(index); //removes the book from the user's list of books loaned.
		} else {
			//the list of books the user has loaned out does not include the book ID of the book specified.
			System.out.println("You do not have this book on loan to return.");
		}
		getMainMenu();
	}
	
	/**
	 * This method allows the user to return all the books they have on loan.
	 */
	public void returnAllBook() {
		int returnBookID; //container to hold the book ID of a book the user has on loan (in ArrayList booksLoaned)
		if (getListofUsers().get(usrIndex).getBooksLoaned().size() >= 1) { //validates whether the user has at least one book on loan.
			//user has at least one book to return
			for (int i = getListofUsers().get(usrIndex).getBooksLoaned().size()-1; i >= 0; i--) { //references every book in the list of books the user has on loan
				returnBookID = getListofUsers().get(usrIndex).getBooksLoaned().get(i); //gets the book ID of one of the books the user has on loan
				returnBook(validateBook(returnBookID)); //returns the book by validating its index using the validateBook() method.
			}
			System.out.println("All books successfully returned.");
		}
		else {
			//user does not have any books on loan to return
			System.out.println("You do not have any books on loan to return.");
		}
		getMainMenu();
	}
	
	/**
	 * This method displays a GUI with the book's cover image and a general description of the book's states.
	 * @param index - the index of the book object the user wants to see the cover image of in the ArrayList listofBooks.
	 *
	 */

	public void getCover(int index){
		if (listofBooks.get(index).getCoverURL() != null) { //validates whether a cover URL state exists (not null) for the book object.
			//coverURL does exist (not null)
			try {
				URL imageLocation = new URL(listofBooks.get(index).getCoverURL());
				//creates a new url object with the String cover url state of the book
				//GUI window that displays the book's cover image and the book's states.
				JOptionPane.showMessageDialog(null,
						"|Genre: " + listofBooks.get(index).getGenre() + "|\n|Availability status: "
								+ listofBooks.get(index).isOnLoan() + "|\n|Times loaned: "
								+ listofBooks.get(index).getNumofLoan() + "|" + "\n|Quantity:"
								+ listofBooks.get(index).getQuantity() + "|\n|Quantity on Loan: "
								+ listofBooks.get(index).getQuantityOnLoan() + "|",
						listofBooks.get(index).getTitle() + " by " + listofBooks.get(index).getAuthor(),
						JOptionPane.PLAIN_MESSAGE, new ImageIcon(imageLocation));
			} catch (MalformedURLException e) {
				//cover URL String is not formatted correctly - cannot display cover image.
				e.printStackTrace();
			}
		} else {
			//coverURL does not exist (coverURL = null)
			System.out.println("This book does not have a cover image.");
		}
		getMainMenu();
	}

	/**
	 * This method prints out a description of the book's states in the library.
	 * @param index - the index of the book object the user wants to get the content description of in the ArrayList listofBooks.
	 */
	public void getBookDetails(int index) {
		System.out.println(listofBooks.get(index).getBookID() + ": " + listofBooks.get(index).getTitle() + " by "
				+ listofBooks.get(index).getAuthor() + " |Genre: " + listofBooks.get(index).getGenre()
				+ "|\n  |Available: " + listofBooks.get(index).isOnLoan() + "|  |Times loaned: "
				+ listofBooks.get(index).getNumofLoan() + "|" + "|  |Quantity:" + listofBooks.get(index).getQuantity()
				+ "|  |Quantity on Loan: " + listofBooks.get(index).getQuantityOnLoan() + "|\n");
	}
	
	/**
	 * This method prints out the entire book content of the library.
	 */
	public void getContents() {
		System.out.println("--------------------------------------");
		System.out.println(getLibName() + ": BOOK CONTENT\n--------------------------------------");
		for (int i = 0; i < this.listofBooks.size(); i++) { //every book element referenced
			getBookDetails(i); //print out book's details (states) using the getBookDetails() method.
		}
		System.out.println("--------------------------------------");
	}
	
	/**
	 * This accessor calculates the number of book objects with state availability being true (able to loan) and returns it.
	 * @return - number of book objects available to be loaned
	 */
	public int getBooksAvailable() {
		int booksAvail = 0; //container to hold  the number of book objects available (default zero - none)
		for (int i = 0; i < getListofBooks().size(); i++) { //references every book object in the list of books
			if (listofBooks.get(i).isOnLoan() == true) { //validates whether the book is available (book state is true)
				booksAvail++; //increments for each book available in the list of books ArrayList.
			}
		}
		return booksAvail; // returns number of books with availability status true
	}
	
	/**
	 * This method allows the user to search through the book content in the library by the author, book title or genre book states.
	 */

	public void search() {
		int numSearchTerms = 0; //default number of search terms - resets for every new search.
		while (validInput == false) {
			System.out.println("Which state do you want to search by:\n(AUTHOR)BookAuthor\n(TITLE)Book Title\n(GENRE)Genre");
			str_userChoice = sc.nextLine(); //user input for choosing which state to search by
			if (str_userChoice.equalsIgnoreCase("TITLE")) { //user chooses to search by book title
				System.out.println("Please type in search term (starts with): ");
				str_userChoice = sc.nextLine(); //asks the user what title to search for
				System.out.println("-------------------");
				for (int index = 0; index < listofBooks.size(); index++) { //accesses every element of the arrayList
					if (listofBooks.get(index).getTitle().startsWith(str_userChoice)) { //validates whether the user's input is the start of a book's String title - a match.
						//user's input matches or partially matches a title (title starts with the user's input) in the list of books
						numSearchTerms++; //increments for each match
						getBookDetails(index); //prints out details of matched book
					}
				}
				validInput = true; //searching finished
			} else if (str_userChoice.equalsIgnoreCase("AUTHOR")) {
				System.out.println("Please type in search term (starts with):");
				str_userChoice = sc.nextLine();
				System.out.println("-------------------");
				for (int index = 0; index < listofBooks.size(); index++) {
					if (listofBooks.get(index).getAuthor().startsWith(str_userChoice)) {
						numSearchTerms++;
						getBookDetails(index);
					}
				}
				validInput = true;
			} else if (str_userChoice.equalsIgnoreCase("GENRE")) {
				BookGenres genre; //container for holding user's genre choice
				genre = validateGenreExtend(); //calls a method which returns the user's BookGenres genre choice
				System.out.println("-------------------");
				for (int index = 0; index < listofBooks.size(); index++) {
					if (listofBooks.get(index).getGenre() == genre) {
						numSearchTerms++;
						getBookDetails(index);
					}
				}
				validInput = true;
			}
			else {
				//user's input is not valid - not one of the book state options available to search by
				System.out.println("Please enter a valid state to search by.");
			}
		}
		System.out.println("-------------------\nNumber of books found: " + numSearchTerms);
		validInput = false;
	}
	
	/**
	 * This method allows the user to choose between displaying the books ordered by quantity or quantity on loan book state and prints the sorted book content
	 * in descending order.
	 */
	
	public void sortBooks() {
		listofBooksSorted = new ArrayList<Book>(listofBooks); //creates a copy of listofBooks -  means sorting does not manipulate the master ArrayList listofBooks.
		while (validInput == false) {
			System.out.println("Which state do you want to sort by in descending order?: \n(QUANTITY)Quantity\n(ONLOAN)Quantity On Loan");
			str_userChoice = sc.nextLine(); //user input for choosing what state to sort by
			if (str_userChoice.equalsIgnoreCase("QUANTITY") || str_userChoice.equalsIgnoreCase("ONLOAN")) { //validates the user input is either of the two available options
				if (str_userChoice.equalsIgnoreCase("QUANTITY")) {
					//sort by quantity
					sortQuantity(); //calls a method that sorts ArrayList listofBookSorted by quantity book state descending.
				} 
				else if (str_userChoice.equalsIgnoreCase("ONLOAN")) {
					//sort by quantity on loan
					sortOnLoan(); //method that sorts by quantity on loan book state descending.
				}
				System.out.println("Ordered list\n-------------------");
				for (int i = listofBooksSorted.size()-1; i >= 0; i--) { //references every element in the now sorted book list.
					//prints out the states of each ordered book.
					System.out.println(listofBooksSorted.get(i).getBookID() + ": " + listofBooksSorted.get(i).getTitle() + " by "
								+ listofBooksSorted.get(i).getAuthor() + " |Genre: " + listofBooksSorted.get(i).getGenre()
								+ "|\n  |Available: " + listofBooksSorted.get(i).isOnLoan() + "|  |Times loaned: "
								+ listofBooksSorted.get(i).getNumofLoan() + "|" + "|  |Quantity:" + listofBooksSorted.get(i).getQuantity()
								+ "|  |Quantity on Loan: " + listofBooksSorted.get(i).getQuantityOnLoan() + "|\n");
			}
				validInput = true; //sorting finished
		}
			else {
				//user's input is not valid - not one of the states available to sort by 
				System.out.println("Invalid state entered. Please try again.");
			}
	}
		System.out.println("-------------------");
		validInput = false;
		}

	/**
	 * This method sorts a copy of the list of books by quantity book state descending.
	 */
	public void sortQuantity() {
		for (int i = listofBooksSorted.size() - 1; i >= 0; i--) { //i is every element in the library's book content ArrayList and through each iteration decrements, restricting the range.
			for (int j = 0; j < i; j++) { //j is the first element incrementing up to the limit (i)
				if (listofBooksSorted.get(j).getQuantity() > listofBooksSorted.get(j + 1).getQuantity()) { //validates whether the the first index's quantity is greater than the next (second) index's quantity
					Book temp = listofBooksSorted.get(j + 1); //creates a temporary book object that contains a copy of the next book object that has a smaller quantity than the previous.
					listofBooksSorted.set(j + 1, listofBooksSorted.get(j)); //shifts the previous (first) index's element along to the index of the next (second) quantity.
					listofBooksSorted.set(j, temp); //shifts the previous (next) index's smaller quantity behind the previous (first) quantity.
				}
			}
		}
	}
	
	/**
	 * This method sorts a copy of the list of books by quantity on loan book state descending.
	 */
	public void sortOnLoan() {
		for (int i = listofBooksSorted.size() - 1; i >= 0; i--) {
			for (int j = 0; j < i; j++) {
				if (listofBooksSorted.get(j).getQuantityOnLoan() > listofBooksSorted.get(j + 1).getQuantityOnLoan()) {
					Book temp = listofBooksSorted.get(j + 1);
					listofBooksSorted.set(j + 1, listofBooksSorted.get(j));
					listofBooksSorted.set(j, temp);
				}
			}
		}
	}
	
	/**
	 * This method prints out the main console menu to the user and whether the full range of options is print is
	 * dependent on the user's librarian status.
	 */

	public void getMainMenu() {
		System.out.println("--------------------------------------");
		if (isUsrLibrarianStatus()) { //validates whether the user is a librarian
			//user is a librarian
			System.out.println( //menu for librarians
					"LIBRARIAN MENU\n-------------------\n(ADD) Add a new Book\t(ADDU) Add user\n(EDIT) Edit a book\t(EDITU) Edit your details\n(DEL) Delete a book\t(SHOWU) Show your details\n(LOAN) Loan a book\t(SHOWALLU) Show ALL users details\n"
							+ "(RETURN) Return a book\t(EDITOTHERU) Edit other users\n(RETURNALL) Return all books\t(DELETE) Delete account\n(DUE) Books Due\t\t(EXIT) Exit Library\n(SEARCH) Search Books\n(STATS) Show Book Statistics\n(SHOWB) Show contents\n(COVER) Get Cover Image\n(MENU) Bring up menu\n-------------------");
		} else { 
			//user is not a librarian
			System.out.println( //menu for non-librarians
					"USER MENU\n-------------------\n(LOAN) Loan a book\t\t(SHOWU) Show your details"
					+ "\n(RETURN) Return a book\t\t(SHOWALLU) Show all users\n(RETURNALL) Return all books\t"
					+ "(DELETE) Delete account\n(DUE) Books Due"
                    + "\t\t\t(EDITU) Edit your details\n(SHOWB) Show contents\t\t"
					+ "(MENU) Bring up menu\n(COVER) Get cover image\t\t"
					+ "(EXIT) Exit\n(SEARCH) Search Books"
					+ "\n(STATS) Show Book Statistics\n-------------------");
		}
	}
	
	/**
	 * This method is used to add a new user to the library. It is mainly used during the setup of the program
	 * to create new users with the inputs scanned from the user content .txt file.
	 * @param usrName - user's full name
	 * @param usrID - user's unique ID
	 * @param usrPassword - user's password
	 * @param librarianStatus - user's access rights, if true allows them to manipulate the library's books and other users.
	 * @param booksLoaned - list of books referenced by their book ID that the user has currently loaned out.
	 */

	public void addUser(String usrName, int usrID, String usrPassword, boolean librarianStatus,
			ArrayList<Integer> booksLoaned) {
		User newUser = new User(usrName, usrID, usrPassword, librarianStatus, booksLoaned); //creates a new user object with states
		listofUsers.add(newUser); //catalogues the user in the library's user content, adding the newly created object to ArrayList listofUsers.
	}
	
	/**
	 * This method allows the user to add a new user to the library, providing input for the new user's states before
	 * calling the addUser method to create a new user object.
	 */

	public void addUserExtend() {
		//containers for the user's new user state input.
		String usrName = ""; //new user's full name
		String usrPass = ""; //new user's password
		boolean librarianStatus = false; //by default the user is a non-librarian
		ArrayList<Integer> usrBooksLoaned = new ArrayList<Integer>(); //list of books the user has on loan

		System.out.println("Please type in your name: ");
		usrName = sc.nextLine(); //input for the user to enter the new user's full name
		while (validInput == false) {
			System.out.println("Please type in a password (at least six characters): ");
			usrPass = sc.nextLine(); //new user's password
			if (usrPass.length() >= 6) { //validates the user's password is secure - at least six characters in length.
				validInput = true; //password is valid
			}
		}
		validInput = false;
		
		librarianStatus = validateLibrarian(); //calls a method that allows the user to input the new user's librarian status.
		setUsrMaxID(getUsrMaxID() + 1); //increments the max user ID - to be used as the user ID of the new user
		System.out.println("The new user's ID is " + getUsrMaxID()); //prints out the new user's ID that can be used to log in with.
		addUser(usrName, getUsrMaxID(), usrPass, librarianStatus, usrBooksLoaned); //calls the addUser() method to create a new user object and catalogue it in the library's user content list.
	}

	/**
	 * This method prints out the details of a single user.
	 * @param index - the user's index in the ArrayList listofUsers
	 */
	public void getUsrDetails(int index) {
		//printing out of general details (states)
		System.out.print("\n" + listofUsers.get(index).getUsrID() + ": " + listofUsers.get(index).getUsrName()
				+ " |Books owed: " + listofUsers.get(index).getBooksLoaned().toString() + " | |Status: ");
		//validates whether the user is a librarian (true) or a non-librarian (false) and prints out their usage privilege status
		if (listofUsers.get(index).getLibrarianStatus()) {
			//user is a librarian
			System.out.print("Librarian|");
		} else {
			//non-librarian
			System.out.print("User|");
			//validates whether the current user is a librarian
			if (listofUsers.get(usrIndex).getLibrarianStatus()) {
				//prints out the passwords of all non-librarians for a  librarian status user
				System.out.print(" |Password: " + listofUsers.get(index).getUsrPassword() + "|");
			}
		}
		System.out.println();
	}
	
	/**
	 * This method prints out the details of every user in the library by looping the getUserDetails() method.
	 */
	public void getUsrDetailsTotal() {
		System.out.println(getLibName() + ": User Content \n-------------------");
		for (int i = 0; i < listofUsers.size(); i++) { //refers to every user object in the listofUsers ArrayList - all users
			getUsrDetails(i); //prints out the details of each user
		}
		System.out.println("-------------------");
	}

	/**
	 * This method prints out the book ID and book title of the books the current user has on loan.
	 * @param index - the current user's index in the ArrayList listofUsers (library's user content)
	 */
	public void getBooksDue(int index) {
		int bookID; //container to hold a book ID matched from the user's list of books loaned.
		//validates whether the user has books on loan
		if (listofUsers.get(index).getBooksLoaned().size() == 0) {
			//user does not have any books on loan
			System.out.println("You have no books due.");
			getMainMenu();
		} else {
			//user has books on loan (due)
			System.out.println("BOOKS DUE\n-------------------");
			for (int i = 0; i < listofUsers.get(index).getBooksLoaned().size(); i++) { //references every book on loan in the library's book content.
				bookID = listofUsers.get(index).getBooksLoaned().get(i); //gets the book ID of a book loaned by the user
				for (int j = 0; j < listofBooks.size(); j++) {  //references every book object to find a matching book ID
					if (listofBooks.get(j).getBookID() == bookID) { //matches the book ID with the book object element in the list of Books
						System.out.println(bookID + ": " + listofBooks.get(j).getTitle()); //prints out book ID and title state of the matched book.
					}
				}
			}
		}
	}
	/**
	 * This method allows the user to edit a user's details (user object states)
	 * @param index - the current user's index in the ArrayList listofUsers
	 */
	public void editUserDetails(int index) {
		System.out.println("What do you wish to edit?:\n(NAME)Full Name\n(PASS)User Password");
		if (getListofUsers().get(usrID).getLibrarianStatus()) { //validates whether the user is a librarian
			//user is a librarian
			//menu option to edit user privilege only available for librarians
			System.out.println("(STATUS)User privilege status"); 
		}
		str_userChoice = sc.nextLine(); //input for the user to choose which user state they want to edit
		
		if (str_userChoice.equalsIgnoreCase("NAME")) { //user selects full name
			System.out.println("Please enter a new name:");
			str_userChoice = sc.nextLine(); //input for the user to enter a new name
			listofUsers.get(index).setUsrName(str_userChoice); //sets user's new full name to the user's input
			System.out.println("Name successfully changed.");
		} else if (str_userChoice.equalsIgnoreCase("PASS")) { //user selects user password
			System.out.println("Please enter a new password (at least six characters)");
			while (validInput == false) {
				str_userChoice = sc.nextLine(); //input for the user to enter a new password
				if (str_userChoice.length() >= 6) { //validates the password is secure - greater than or equal to six characters.
					validInput = true; //password is valid
				}
				else {
					//password is not valid - less than six characters
					System.out.println("Password must be at least six characters. Please try again.");
				}
			}
			validInput = false;
			listofUsers.get(index).setUsrPassword(str_userChoice); //sets the current user's password to the user's input.
			System.out.println("Password successfully changed."); 
		}
		else if (str_userChoice.equals("STATUS") && (getListofUsers().get(usrIndex).getLibrarianStatus())) { //validates that the user is a librarian, user must be to edit librarian status
			listofUsers.get(index).setLibrarianStatus(validateLibrarian()); //sets usage status to the return of the validateLibrarian(), which validates the user input and returns it as 
			// true (librarian) or false (non-librarian)
		}
		getMainMenu();
	}
	
	/**
	 * This method allows the user to delete their user account from the library.
	 */
	public void deleteUser() {
		if (getListofUsers().get(usrIndex).getBooksLoaned().size() > 0) {  //validates whether the user has books on loan
			//user has books on loan - cannot delete until books are returned.
			System.out.println("An user account cannot be deleted while the user has books out on loan.");
		} else {
			System.out.println("Are you sure you want to delete your account? Press y to confirm.");
			str_userChoice = sc.nextLine(); //user input for confirmation
			
			if (str_userChoice.equals("y")) { //validates whether the user has confirmed they want to delete their account
				//user confirms they want to delete their account
				getListofUsers().remove(usrIndex); //removes account from the library's user content
				exit(); //exits the library to save changes
			} else {
				//user chooses not to delete their account (input not 'y')
				System.out.println("You have decided not to delete your user account");
			}
		}
	}
	/**
	 * This method validates the user's input book ID exists as a
	 * book ID state of an element of list of books ArrayList and returns the index of the element.
	 * @return - the index of the book object in the listofBooks ArrayList.
	 */
	public int validateBookExtend() {
		int bookIndex = -1; //container to hold book index (default -1)
		while (bookIndex == -1) { //loops until book ID is not set to default (-1)
			System.out.println("What is the book ID?: ");
			if (sc.hasNextInt()) { //validates whether the user's input is an integer
				int_userChoice = sc.nextInt(); //input for the user to enter a book ID
				bookIndex = validateBook(int_userChoice); //sets book ID to the return value of the validateBook(), -1 if the ID is not valid 
				//or the index of the book object in the library's list of books if it is valid.
			}
			sc.nextLine();
		}
		return bookIndex; //returns index of the book element
	}
	
	/**
	 * This method validates if a book object in the library's book content has the method's parameter book ID as a state and returns the index of the book object
	 *  if a match is found. Returns -1 if no match is found.
	 * @param bookID
	 * @return - index of the matched or unmatched book object.
	 */
	public int validateBook(int bookID) {
		for (int i = 0; i < getListofBooks().size(); i++) { //references every book in the library's list of books. 
			if (getListofBooks().get(i).getBookID() == bookID) {//validates whether the user's input book ID matches a book in the library's ID.
				return i; //returns valid index in the ArrayList listofBooks of matching book object with the same book ID as the user input.
			}
		}
		return -1; //no match found
	}
	
	
	/**
	 * This method translates a book genre argument of type String to BookGenres type. Mostly used during the
	 * opening setup stages of the main method, loading in states from the book content .txt file.
	 * @param genre - genre of book as a String
	 * @return - genre of book as BookGenres enum type.
	 */
	public BookGenres validateGenre(String genre) {
		//switch statement that validates genre state as type String and returns it as type BookGenres enum.
		switch (genre) {
		case "FANTASY":
			return BookGenres.FANTASY;
		case "HORROR":
			return BookGenres.HORROR;
		case "CRIME":
			return BookGenres.CRIME;
		case "COMEDY":
			return BookGenres.COMEDY;
		case "ADVENTURE":
			return BookGenres.ADVENTURE;
		case "CHILDRENS":
			return BookGenres.CHILDRENS;
		case "COMIC":
			return BookGenres.COMIC;
		case "DRAMA":
			return BookGenres.DRAMA;
		case "HISTORICAL":
			return BookGenres.HISTORICAL;
		case "SCIENCE":
			return BookGenres.SCIENCE;
		case "ACADEMIC":
			return BookGenres.ACADEMIC;
		case "BIOGRAPHY":
			return BookGenres.BIOGRAPHY;
		case "AUTOBIOGRAPHY":
			return BookGenres.AUTOBIOGRAPHY;
		default: //String genre cannot be sorted to a relevant BookGenres enum
			return BookGenres.UNCATEGORISED;  
		}
	}
	/**
	 * This method allows the user to input a choice of genre for a book and validates it by calling the validateGenre() method.
	 * @return - user's choice of genre as type BookGenres enum.
	 */
	public BookGenres validateGenreExtend() {
		String genre = ""; //container to hold user's input of genre choice

		System.out.println(
				"Please enter the genre (case sensitive):\nOptions: FANTASY, HORROR, CRIME, COMEDY, ADVENTURE, CHILDRENS, COMIC, DRAMA, HISTORICAL, SCIENCE,\nACADEMIC, BIOGRAPHY, AUTOBIOGRAPHY");
		genre = sc.nextLine(); //input for the user to enter a choice of genre
		return validateGenre(genre); //calls the validateGenre to validate the user's input String genre
	}
	
	/**
	 * This method validates whether the user input cover URL is a valid formatted URL -> MalformedURLException is thrown if not.
	 * @param coverURL - the user's input cover URL as type String
	 * @return - either the user input cover URL if it's valid or null if not.
	 */
	public String validateURL(String coverURL) {
		try {
			URL testURL = new URL(coverURL);  //creates a new URL object, will throw MalformedURLException if an URL object cannot be created.
			return coverURL; //String URL is valid and returned
		} catch (MalformedURLException e) { 
			//user input String URL is not a formatted URL - cannot create URL object from it.
			System.out.println("Cover URL is not valid. Setting cover URL to null.");
			return null; //returns an empty cover URL
		}
	}
	
	/**
	 * This method validates and returns a user's index in listofUsers ArrayList
	 * @param usrID - user ID of a user
	 * @return - returns a user's index.
	 */

	public int validateUser(int usrID) {
		for (int i = 0; i < getListofUsers().size(); i++) { //reference every user - every user object element in listofUsers
			if (getListofUsers().get(i).getUsrID() == usrID) { //validates whether the user object's ID at index i matches the current user's ID
				return i; //returns index of matched user object.
			}
		}
		return -1; //reached if the user input user ID is not a state of any book object in listofUsers.
	}
	/**
	 * This method validates and returns another user's index in the library's user content (listofUsers ArrayList_.
	 * @return
	 */
	public int validateOtherUser() {
		int index = -1; //container for user's input of an other user's user ID.
		while (index == -1) { //validate index has not changed as it is at default -1, which is not an valid index.
			System.out.println("Which user (referred to by their ID) do you wish to edit?");
			if (sc.hasNextInt()) { //validates whether the user has entered an integer
				int_userChoice = sc.nextInt(); //asks user for another user's userID
				index = validateUser(int_userChoice); //calls the validateUser method to validate the user ID exists a user. (if not, index = -1)
			}
			sc.nextLine();
		}
		return index; //returns the index of the other user the user wishes to edit.

	}
	
	/**
	 * This method allows the user to choose what librarian status (librarian/non-librarian) state a user should have.
	 * @return - boolean that denotes either the user is a librarian (true) or non-librarian (false)
	 */
	public boolean validateLibrarian() {
		boolean librarianStatus = false; //container to hold user's librarian status state
		System.out.println(
				"Should the user be a librarian? Press y if true. Press any other key if false.");
		str_userChoice = sc.nextLine(); //input for the new user's permission rights (librarian status)
		if (str_userChoice.equalsIgnoreCase("y")) {
			librarianStatus = true; //new user is a librarian
		} 
		return librarianStatus; //returns library usage state of the user
	}
	
	/**
	 * This method allows a librarian to edit another user's details (states)
	 */
	public void editOtherUserDetails() {
		editUserDetails(validateOtherUser()); //calls the edit user details to edit the other user's details 
        //by retrieving their index in ArrayList listofUsers through the validateOtherUser() method and passing it to 
		//the editUserDetails method.
	}

	/**
	 * This method terminates the program and gives the user the option to have a persistent save  
	 * of the current state of the library's books and users by writing and printing to .txt book/user content files.
	 */

	public void exit() {
		File BookFile = new File(getBookDirect()); //new file object created with the directory to book content .txt file
		File UserFile = new File(getUsrDirect()); //user content .txt file
		
		//containers to hold book states to be written and printed to the .txt file.
		int bookID = 0;
		String title = "";
		String author = "";
		BookGenres genre;
		int numTimesLoaned = 0;
		String loanStatus = "";
		int quantity = 0;
		int quantityOnLoan = 0;
		String coverURL = "";

		//containers to hold user states
		String usrName = "";
		int usrID = 0;
		String usrPass = "";
		boolean status = false;
		ArrayList<Integer> usrBooksLoaned = new ArrayList<Integer>(); 

		System.out.println("Save and exit (y to confirm)?");
		str_userChoice = sc.nextLine(); //input for the user to choose whether they want exit, or exit and save.
		if (str_userChoice.equalsIgnoreCase("y")) {
			//save and exit
			try {
				BookFile.delete(); //deletes .txt file used to scan input from during loading book/user content - to be replaced
				BookFile.createNewFile(); //new blank file created for book states to be written and printed to.
				
				//creation of FileWriter, BufferedWriter and PrintWriter objects for the book content .txt file - allows for writing and printing to .txt files.
				FileWriter bookFileWriter = new FileWriter(BookFile, true);
				BufferedWriter bookBufferedWriter = new BufferedWriter(bookFileWriter);
				PrintWriter bookPrintWriter = new PrintWriter(bookBufferedWriter, true);
				
				for (int i = 0; i < getListofBooks().size(); i++) { //iterates for every book object
					//containers being assigned with the current index (i) book's states
					bookID = getListofBooks().get(i).getBookID();
					title = getListofBooks().get(i).getTitle();
					author = getListofBooks().get(i).getAuthor();
					genre = getListofBooks().get(i).getGenre();
					numTimesLoaned = getListofBooks().get(i).getNumofLoan();
					quantity = getListofBooks().get(i).getQuantity();
					quantityOnLoan = getListofBooks().get(i).getQuantityOnLoan();
					coverURL = getListofBooks().get(i).getCoverURL();

					if (getListofBooks().get(i).isOnLoan() == false) { //validates whether the book is available or not available
						//book is not available
						loanStatus = "n"; //alternative to false being written to the text file.
					} else { 
						//book is available
						loanStatus = "y"; 
					}
					
					//using PrintWriter, prints the current index (i) book's states to the new .txt file.
					bookPrintWriter.println(bookID);
					bookPrintWriter.println(title);
					bookPrintWriter.println(author);
					bookPrintWriter.println(genre);
					bookPrintWriter.println(numTimesLoaned);
					bookPrintWriter.println(loanStatus);
					bookPrintWriter.println(quantity);
					bookPrintWriter.println(quantityOnLoan);
					bookPrintWriter.println(coverURL);
				}
				//closes these objects as there is no need for them anymore - writing and printing to book content .txt file complete
				bookFileWriter.close();
				bookBufferedWriter.close();
				bookPrintWriter.close();
				
				UserFile.delete(); //deletes previously scanned .txt file containing the list of users - to be replaced.
				UserFile.createNewFile(); //new blank file created to be written and printed to
				
				//creation of FileWriter, BufferedWriter and PrintWriter objects for the user content .txt file.
				FileWriter userFileWriter = new FileWriter(UserFile, true);
				BufferedWriter userBufferedWriter = new BufferedWriter(userFileWriter);
				PrintWriter userPrintWriter = new PrintWriter(userBufferedWriter, true);

				for (int i = 0; i < getListofUsers().size(); i++) { //iterates for every user object
					//containers assigned to current user index (i) states - to be printed to .txt file
					usrID = getListofUsers().get(i).getUsrID();
					usrName = getListofUsers().get(i).getUsrName();
					usrPass = getListofUsers().get(i).getUsrPassword();
					status = getListofUsers().get(i).getLibrarianStatus();
					usrBooksLoaned = getListofUsers().get(i).getBooksLoaned();

					//prints the current index (i) user's states to the user content .txt file.
					userPrintWriter.println(usrName);
					userPrintWriter.println(usrID);
					userPrintWriter.println(usrPass);
					userPrintWriter.println(status);

					//prints each book ID element of the user's list of books loaned out on a new line to the user content .txt file
					for (int j = 0; j < getListofUsers().get(i).getBooksLoaned().size(); j++) {
						userPrintWriter.println(usrBooksLoaned.get(j));
					}
				}
				//objects closed - writing and printing to user content .txt file complete
				userFileWriter.close();
				userBufferedWriter.close();
				userPrintWriter.close();
			} catch (IOException e) {
				//input or output error -  writing or printing to .txt file not successful
				e.printStackTrace();
			}
		System.out.println("Exiting library...");
	}
	}

	/**
	 * This method returns the list of books in the library
	 * @return - list of books in the library
	 */
	public ArrayList<Book> getListofBooks() {
		return listofBooks;
	}

	/**
	 * This method returns the list of users in the library.
	 * @return - list of users in the library
	 */
	public ArrayList<User> getListofUsers() {
		return listofUsers;
	}

	/**
	 * This method returns the name of the library assigned to the library.
	 * @return - library's name
	 */
	public String getLibName() {
		return libName;
	}

	/**
	 * This method sets new information into the library name instance variable of the Library object.
	 * @param libName - the library's new name.
	 */
	public void setLibName(String libName) {
		this.libName = libName;
	}
	
	/**
	 * This method returns the library's address assigned to the library.
	 * @return - library's address
	 */
	public String getLibAddress() {
		return libAddress;
	}
	
	/**
	 * This method sets new information into the library address instance variable of the Library object.
	 * @param libAddress - the library's new address
	 */
	public void setLibAddress(String libAddress) {
		this.libAddress = libAddress;
	}

	/**
	 * This method returns the directory of the book content .txt file.
	 * @return - directory of the book content .txt file
	 */
	public String getBookDirect() {
		return bookDirect;
	}

	/**
	 * This method sets new information into the book file directory variable.
	 * @param bookDirect - new directory of the book content .txt file
	 */
	public void setBookDirect(String bookDirect) {
		this.bookDirect = bookDirect;
	}

	/**
	 * This method returns the directory of the user content .txt file.
	 * @return - directory of the user content .txt file
	 */
	public String getUsrDirect() {
		return usrDirect;
	}

	/**
	 * This method sets new information into the user file directory variable.
	 * @param usrDirect - new directory of the user content .txt file
	 */
	public void setUsrDirect(String usrDirect) {
		this.usrDirect = usrDirect;
	}

	/**
	 * This method returns the user ID of the current user.
	 * @return - the current user's ID
	 */
	public int getUsrID() {
		return usrID;
	}

	/**
	 * This method sets new information into the user ID variable.
	 * @param usrID - new current user ID
	 */
	public void setUsrID(int usrID) {
		this.usrID = usrID;
	}

	/**
	 * This method returns the current user's element index in ArrayList listofUsers
	 * @return - current user's index
	 */
	public int getUsrIndex() {
		return usrIndex;
	}

	/**
	 * This method sets new information into the user index variable.
	 * @param usrIndex - user's new current index in listofUsers ArrayList
	 */
	public void setUsrIndex(int usrIndex) {
		this.usrIndex = usrIndex;
	}

	/**
	 * This method returns the greatest book ID state of books in the library's book content.
	 * @return - max ID of list of books element's book ID state.
	 */
	public int getMaxID() {
		return maxID;
	}

	/**
	 * This method sets new information into the maximum book ID variable. 
	 * @param maxID - the new greatest book ID
	 */
	public void setMaxID(int maxID) {
		this.maxID = maxID;
	}

	/**
	 * This method returns the greatest user ID state of user in the library's user content.
	 * @return - max ID of list of users element's user ID state.
	 */
	public int getUsrMaxID() {
		return usrMaxID;
	}

	/**
	 * This method sets new information into the maximum user ID variable. 
	 * @param usrMaxID - the new greatest user ID
	 */
	public void setUsrMaxID(int usrMaxID) {
		this.usrMaxID = usrMaxID;
	}

	/**
	 * This method returns the current user's librarian status.
	 * @return - the librarian status of the current user.
	 */
	public boolean isUsrLibrarianStatus() {
		return usrLibrarianStatus;
	}

	/**
	 * This method sets new information into the user's librarian status variable.
	 * @param usrLibrarianStatus - the new librarian status of the current user.
	 */
	public void setUsrLibrarianStatus(boolean usrLibrarianStatus) {
		this.usrLibrarianStatus = usrLibrarianStatus;
	}

}
