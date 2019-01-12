package libraryAssignment;

/**
 * This class models a book and defines its functions.
 * @author 40156063
 */
public class Book {
	//instance variables
	private String title;
	private String author;
	BookGenres genre;
	private int bookID;
	private int numofLoan;
	private boolean onLoan;
	private int quantity;
	private int quantityOnLoan;
	private String coverURL;

	/**
	 * This is the constructor method for the book object.
	 * @param bookID - book ID of the book
	 * @param title - title of the book
	 * @param author - author of the book
	 * @param genre - genre of the book
	 * @param numofLoan - number of times the book has been loaned 
	 * @param onLoan - availability status of the book
	 * @param quantity - quantity of the book
	 * @param quantityOnLoan - quantity of the book on loan
	 * @param coverURL - String URL of the book's cover image URL
	 */
	public Book(int bookID, String title, String author, BookGenres genre, int numofLoan, boolean onLoan, int quantity,
			int quantityOnLoan, String coverURL) {
		this.bookID = bookID;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.numofLoan = numofLoan;
		this.onLoan = onLoan;
		this.quantity = quantity;
		this.quantityOnLoan = quantityOnLoan;
		this.coverURL = coverURL;
	}
	/**
	 * This method returns the title of the book
	 * @return - title of the book
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * This method returns the author of the book
	 * @return - author of the book
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * This method returns the unique book ID of the book
	 * @return - book ID of the book
	 */
	public int getBookID() {
		return bookID;
	}

	/**
	 * This method returns the number of times the book has been loaned out.
	 * @return - number of times the book has been loaned out.
	 */
	public int getNumofLoan() {
		return numofLoan;
	}

	/**
	 * This method returns the availability status of the book
	 * @return - availability status of the book
	 */
	public boolean isOnLoan() {
		return onLoan;
	}

	/**
	 * This method returns the quantity of the book.
	 * @return - quantity of the book
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * This method returns the quantity of book on loan.
	 * @return - quantity of book on loan
	 */
	public int getQuantityOnLoan() {
		return quantityOnLoan;
	}
	
	/**
	 * This method returns the String cover URL of the book.
	 * @return - cover URL of the book
	 */
	public String getCoverURL() {
		return coverURL;
	}

	/**
	 * This method sets new information into the book ID instance variable
	 * of the book object.
	 * @param bookID - the new book ID
	 */
	
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	/**
	 * This method sets new information into the book title instance variable
	 * of the book object.
	 * @param title - the new book title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * This method sets new information into the book author instance variable
	 * of the book object.
	 * @param author - the new book author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * This method sets new information into the book number of times loaned instance variable
	 * of the book object.
	 * @param numofLoan - the new number of times the book has been loaned
	 */
	public void setNumofLoan(int numofLoan) {
		this.numofLoan = numofLoan;
	}
	
	/**
	 * This method sets new information into the book quantity instance variable
	 * of the book object.
	 * @param quantity - the new book quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * This method sets new information into the book quantity on loan instance variable
	 * of the book object.
	 * @param quantityOnLoan - the new quantity of the book on loan
	 */
	public void setQuantityOnLoan(int quantityOnLoan) {
		this.quantityOnLoan = quantityOnLoan;
	}

	/**
	 * This method sets new information into the book cover URL instance variable
	 * of the book object.
	 * @param coverURL - the new book cover URL
	 */
	public void setCoverURL(String coverURL) {
		this.coverURL = coverURL;
	}
	
	/**
	 * This method sets new information into the book loan availability instance variable
	 * of the book object.
	 * @param onLoan - the new book loan availability
	 */
	public void setOnLoan(boolean onLoan) {
		this.onLoan = onLoan;
	}
	
	/**
	 * This method returns the genre of book.
	 * @return - genre of the book
	 */
	public BookGenres getGenre() {
		return genre;
	}

	/**
	 * This method sets new information into the book genre instance variable
	 * of the book object.
	 * @param genre - the new book genre
	 */
	public void setGenre(BookGenres genre) {
		this.genre = genre;
	}
}
