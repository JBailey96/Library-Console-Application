package libraryAssignment;

import java.util.ArrayList;
/**
 * This class models a library user and defines the user's functions that manipulate the user's states.
 * @author 40156063
 */
public class User {
	//instance variables
	private String usrName;
	private int usrID;
	private String usrPassword;
	private boolean librarianStatus;
	private ArrayList<Integer> booksLoaned = new ArrayList<Integer>();

	/**
	 * This is the constructor method for the User object. 
	 * @param usrName - full name of the user
	 * @param usrID - ID of the user
	 * @param usrPassword - password of the user
	 * @param librarianStatus - the librarian status of the user (true librarian, false non-librarian)
	 * @param booksLoaned - the list of books (book IDs) the user has on loan.
	 */
	public User(String usrName, int usrID, String usrPassword, boolean librarianStatus, ArrayList<Integer> booksLoaned) {
		this.usrName = usrName;
		this.usrID = usrID;
		this.usrPassword = usrPassword;
		this.booksLoaned = new ArrayList<Integer>(booksLoaned);
		this.librarianStatus = librarianStatus;
	}

	/**
	 * This method returns the full name of the user
	 * @return - user's full name
	 */
	public String getUsrName() {
		return usrName;
	}


	/**
	 * This method sets new information into the full name instance variable
	 * of the user object.
	 * @param usrName - user's new full name
	 */
	
	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}
	
	/**
	 * This method returns the unique user ID of the user
	 * @return - user's user ID
	 */
	public int getUsrID() {
		return usrID;
	}
	
	/**
	 * This method sets new information into the user ID instance variable
	 * of the user object.
	 * @param usrID - user's new ID
	 */
	public void setUsrID(int usrID) {
		this.usrID = usrID;
	}

	/**
	 * This method returns the user's password
	 * @return - user's password
	 */
	public String getUsrPassword() {
		return usrPassword;
	}

	/**
	 * This method sets new information into the user password instance variable
	 * of the user object.
	 * @param usrPassword - user's new password
	 */
	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}

	/**
	 * This method returns the list of books the user has on loan
	 * @return - user's list of books on loan
	 */
	public ArrayList<Integer> getBooksLoaned() {
		return booksLoaned;
	}

	/**
	 * This method sets new information into the list of books loaned instance variable
	 * of the user object.
	 * @param booksLoaned - user's list of books loaned
	 */
	public void setBooksLoaned(ArrayList<Integer> booksLoaned) {
		this.booksLoaned = booksLoaned;
	}
	
	/**
	 * This method returns the librarian status of the user
	 * @return - user's librarian status.
	 */
	public boolean getLibrarianStatus() {
		return librarianStatus;
	}
	
	/**
	 * This method sets new information into the librarian status instance variable
	 * of the user object.
	 * @param librarianStatus - user's new librarian status
	 */
	public void setLibrarianStatus(boolean librarianStatus) {
		this.librarianStatus = librarianStatus;
	}


}
