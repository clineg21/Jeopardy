import java.util.HashMap;
import java.util.Random;

// the board class
public class Board {
	// member variables
	private int[] categories;
	private BoardQuestion[][] board;
	private final int MAXCATEGORIES = 6;
	private final int MAXQUESTIONS = 5;
	private int numdoubles;
	private int qRemaining;
	
	/*
	 * a method to create the board
	 */
	public boolean CreateBoard(Repository repo, int numc, int numq, int numd) {
		numdoubles = numd;
		// validate the number of categories
		if(numc <= 0 || numc > MAXCATEGORIES)
			return false;
		// validate the number of questions
		if(numq <= 0 || numq > MAXQUESTIONS)
			return false;
		// validate the number of daily doubles
		if(numdoubles <= 0 || numdoubles > MAXCATEGORIES)
			return false;
		
		// the number of questions in the board
		// all currently unanswered
		qRemaining = numc * numq;
		
		// create a list for the categories (the first row of the board)
		categories = new int[numc];
		// create the board: questions in columns, categories in rows
		board = new BoardQuestion[numc][numq];
		// return true if the board was created
		return true;
	}
	
	/*
	 * a method to populate the board
	 */
	public void PopulateBoard(Repository repo) {
		// make a random generator
		Random r = new Random();
		
		// get the number of categories and questions
		int numc = board.length, numq = board[0].length;
		
		// create hashmaps to store chosen categories and questions
		HashMap<Integer, Integer> catc = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> questc = new HashMap<Integer, Integer>();
		
		// loop through number of categories chosen
		for(int i = 0; i < numc; i++) {
			// get a random index for a category
			int num = r.nextInt(repo.GetNumCategories()); 
		
			// check if it's already chosen
			while(catc.containsKey(num)) {
				// if it's already chosen, pick until you get a new category
				num = r.nextInt(repo.GetNumCategories());
			}
			
			// add the category to the board
			categories[i] = num;
			
			// add the category to the hashmap
			catc.put(num, num);
			
			// get the chosen category
			Category c = repo.GetCategory(num);

			for(int j = 0; j < numq; j++) {
				// get a random index
				num = r.nextInt(c.GetQuestionSize()); 
				
				// check if its chosen
				while(questc.containsKey(num)) {
					// if it's already chosen, pick until you get a new category
					num = r.nextInt(c.GetQuestionSize());
				}
				
				// create BoardQuestion object
				BoardQuestion bq = new BoardQuestion(c.GetQuestion(num));
				
				//add question to board
				board[i][j] = bq;
				
				// add questions to hashmap
				questc.put(num, num);
			}
			
			// clear questions hashmap
			questc.clear();
		}
		
		// get the daily doubles
		int col, row;
		HashMap<Integer,Integer> doublemap = new HashMap<Integer, Integer>();
		for(int i = 0; i < numdoubles; i++) { 
			// get a random category and question
			col = r.nextInt(numc);
			row = r.nextInt(numq);
			// check if the column contains a double already
			while(doublemap.containsKey(col)) {
				col = r.nextInt(numc);
			}
			// set that board question's daily double to true
			board[col][row].setDailyDouble();
			// add the column to the hashmap
			doublemap.put(col, col);
		}
	}
	
	/*
	 * Method to display the board
	 */
	public String displayToStr(Repository repo) {
		//length of columns and rows
		int col = board.length, row = board[0].length;
		String str = "";
		//print all the columns that are randomly selected from the repository
		//printed in a row
		for(int i = 0; i < col; i++) {
			Category c = repo.GetCategory(categories[i]);
			str += (String.format("%-25s","["+(i+1)+"]"+ c.GetCategory()));
		}
		str += ("%n%n");
		//print all the randomly selected questions from the repository and
		//print them on separate lines
		for(int i = 0;  i < row; i++) {
			for(int j = 0; j < col; j++) {
				//create a question from BoardQuestion
				BoardQuestion q = board[j][i];
				if(q.getAnswered()) {
					str += (String.format("%-25s", "X"));
				}
				else {
					str += (String.format("%-25s", questionValue(i+1)));
				}
			}
			str += ("%n%n");
		}
		return str;
	}
	
	//determines the value for each question
	public int questionValue(int i) {
		//initial value
		int value = 200;
		//initial row that starts after row and increments
		int row = 2;
		//if we are at row 1, return 100 as the monetary value of the question
		if(i == 1) {
			return 100;
		} 
		// at row 2 return 200
		else if(i == 2) {
			return value;
		} 
		// otherwise return the question position * 200
		else {
			while(row < i) {
				value = value + 200;
				row++;
			}
		}
		//return the value of the question
		return value;	
	}

	/*
	 * a method to return the number of unanswered questions in the board
	 */
	public int QuestionsRemaining() {
		return qRemaining;
	}
	
	/*
	 * a method to validate a given category index
	 */
	public boolean CatIsValid(int c) {
		if(c <= 0 || c > categories.length)
			return false;
		return true;
	}
	
	/*
	 * a method to validate a question value
	 */
	public boolean QuestIsValid(int q) {
		for(int i = 1; i <= board[0].length; i++) {
			if(q == questionValue(i))
				return true;
		}
		return false;
	}
	
	/*
	 * a method to validate an answer
	 * is it a char
	 */
	public boolean AnsIsValid(Character a, BoardQuestion q) {
		int max = q.getOptions();
		
		if(97 <= a && (97 + max) > a)
			return true;
		else if(65 <= a && (65 + max) > a)
			return true;
		return false;
	}

	/*
	 * a method to retrieve a question from the board
	 */
	public BoardQuestion RetrieveContent(int category, int question) {
		int q;
		
		// to help retrieve the index 
		if(question <= 400) {
			q = 1;
		} else {
			q = 2;
		}
		
		// get the index- not the point value that the user put in
		question /= 100; 
		
		while(question >= 2) {
			q++;
			question /= 2;
		}
		
		try {
			// get the question if it hasn't been answered
			if(board[category-1][q-1].getAnswered() == false) {
				board[category-1][q-1].setAnswered();
				qRemaining--;
				return board[category-1][q-1];
			}
			// otherwise return null, it was already answered
			else {
				return null;
			}		
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
		
	}
	
	/*
	 * a method to make a guess
	 * returns score to be added (positive if correct,negative if false) or null if an error
	 */
	public Integer MakeGuess(char guess, BoardQuestion question, int score) {
		// convert guess to lower case for convenience
		guess = Character.toLowerCase(guess);
		// convert guess from char to int
		int g = guess - 97;
		
		try {
			// if g is valid
			if(g >= 0 && g < question.getOptions()){
				// if g is correct
				if(question.getIntAnswer() == g)
				{
					// return the points to add
					return score;
				}
				// if g is incorrect
				else
				{
					// return the points to subtract 
					return -1 * score;
				}
			}
			// if the guess is invalid
			else {
				// return points to subtract
				return -1 * score;
			}
		}
		catch(NullPointerException e) {
			return null;
		}
	}
}
