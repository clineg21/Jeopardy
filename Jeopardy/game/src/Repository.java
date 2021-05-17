/*
 * Repository.java reads a formatted test file of categories and questions
 * Format: tag<<>>data
 * Tags: C- category, Q- question; T- true answer, F = false answer
 */
// imports
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

//repository class
public class Repository {
	// variables
	private ArrayList<Category> categories;
	
	/*
	 * The constructor
	 */
	public Repository(String filename) {
		// an ArrayList to hold the categories
		categories = new ArrayList<Category>();
		
		// read the file using given filename
		try {
		readFile(filename);
		} catch (Exception e) {
			System.out.println("Exception reading file: " + e);
		}
	}
	
	/*
	 * a method to get the length of categories
	 */
	public int GetNumCategories() {
		return categories.size();
	}
	
	/*
	 * a method to get an instance of the categories ArrayList
	 */
	public Category GetCategory(int index) {
		return categories.get(index);
	}
	
	/*
	 *  a method to read the file & create data structures
	 */
	public void readFile(String filename) throws Exception {
		// get a scanner to read the file
		File file = new File(filename);
		Scanner s = new Scanner(file);
		 
		// a var for each line of the file
		String line;
		// a variable to hold the most recently created category
		Category c = new Category("");
		// a variable to hold the most recently created question
		Question q = new Question("");
		
		// loop through file
		while(s.hasNextLine()) {
			// read in a line front the file
			line = s.nextLine();
			// get the tag and data from the line
			char tag = line.split("<<>>")[0].charAt(0);
			String data = line.split("<<>>")[1];
			data = data.trim();
			
			// 
			if(tag == 'C') {
				// create a category and add it to the list
				c = new Category(data);
				categories.add(c);
			} else if(tag == 'Q') {
				// create a question and add it to the category
				q = new Question(data);
				c.AddQuestion(q);
			} else if(tag == 'T' || tag == 'F') {
				// create an answer
				q.setAnswer(tag, data);
			} else  {
				System.out.println("ERROR incorrect tag: line <" + line + "> could not be stored");
			}
		}
		// close the scanner
		s.close();
	}
	
	/*
	 *  a method to return the repository as a string
	 */
	public String repoToStr() {
		String s = "";
		for(int i = 0; i < categories.size(); i++) {
			// the current category
			Category c = categories.get(i);
			
			//print the category
			s += c.CategoryToStr() + "\n\n";
		}
		return s;
	}
}
