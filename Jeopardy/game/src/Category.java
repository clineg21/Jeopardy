import java.util.ArrayList;

//category class
public class Category {
	private String category;
	private ArrayList<Question> questions;
	
	/*
	 * Constructor 
	 * assign question to category 
	 */
	public Category(String category) {
		this.category = category;
		questions = new ArrayList<Question>();
	}
	
	/*
	 * Get Category method 
	 * returns the category 
	 * as String
	 */
	public String GetCategory() {
		return category;
	}
	
	/*
	 * Get Question method
	 * returns stored question as a string 
	 */
	public Question GetQuestion(int index) {
		return questions.get(index);	
	}
	
	/*
	 * Get Question Size 
	 * return number of questions in a category 
	 */
	public int GetQuestionSize() {
		return questions.size();	
	}
	
	/*
	 * Add Question Method
	 * adds question to list 
	 */
	public void AddQuestion(Question q) {
		questions.add(q);
	}
	
	/*
	 * returns the category and questions as a formatted string
	 */
	public String CategoryToStr() {
		String s = "";
		
		// add the category to the string
		s += category + "\n";
		
		// add each question (with answers)
		for(int i = 0; i < questions.size(); i ++) {
			s += questions.get(i).questionToStr();
		}
		return s;
	}

	/*
	 * returns question at a given index as a string
	 */
	public String QuestionToStr(int i) {
		return questions.get(i).questionToStr();
	}
}
