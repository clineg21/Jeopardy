import java.util.ArrayList;

public class Question {
	//array of the answers, both the correct and the false answers of size 4s
	private ArrayList<String> answers = new ArrayList<String>();
	//holds the question
	private String question;
	//holds the answer
	private int answer;
	
	//class constructor
	public Question(String question) {
		this.question = question;
	}
	
	//gets the question from the category list
	public String getQuestion()	{
		return question;
	}
	
	//getter to get the false answers; 
	public String getAnswers(int index) {
		return answers.get(index);
	}

	// set the answer & if it's true or false
	public void setAnswer(char value, String a) {
		answers.add(a);
		if(value == 'T') {
			answer = answers.size() - 1;
		} 
	}

	// return the true answer as a string
	public String getTrueAnswer() {
		return answers.get(answer);
	}
	
	// return the index of the true answer
	public int getTrueAnswerInt() {
		return answer;
	}
	
	//return size of answers array
	public int getAnswerSize() {
		return answers.size();
	}
	
	/*
	 * returns the question and answers as a formatted string
	 */
	public String questionToStr() {
		String s = "";
		s += question + "\n";
		for(int i = 0; i < answers.size(); i++) {
			s += "\t"+answers.get(i) + "\n";
		}
		return s;
	}	
}
