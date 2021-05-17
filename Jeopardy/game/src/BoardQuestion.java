
public class BoardQuestion {
	private Question question;
	private boolean answered;
	private boolean daily;
	
	/*
	 * a constructor
	 */
	public BoardQuestion(Question question) {
		this.question = question;
		answered = false;
	}
	
	/*
	 * returns the question text as a string
	 */
	public String QuestionToStr() {
		return question.getQuestion();
	}
	
	/*
	 * a setter for answered: sets to true
	 */
	public void setAnswered() {
		answered = true; 
	}

	/*
	 * returns boolean value of if question has been answered or not
	 */
	public boolean getAnswered() {
		return answered;
	}
	
	/*
	 * sets the question as a daily double
	 */
	public void setDailyDouble() {
		daily = true;
	}
	
	/*
	 * returns if question is a daily double
	 */
	public boolean getDailyDouble() {
		return daily;
	}
	
	/*
	 * gets the correct answer as an int
	 */
	public int getIntAnswer() {
		return question.getTrueAnswerInt();
	}
	
	/*
	 * gets the number of answer options
	 */
	public int getOptions() {
		return question.getAnswerSize();
	}
	
	/*
	 * gets the correct answer as a string
	 */
	public String getStringAnswer() {
		return question.getTrueAnswer();
	}
	
	/*
	 * returns questions and answers as a string
	 */
	public String bqToString() {
		String s = "";
		
		// add question text
		s += "%n" + question.getQuestion() + "%n";
		
		// add answer text (numbered a-z)
		for(int i = 0; i < question.getAnswerSize(); i++) {
			int c = i + 97;
			char qChar = (char)c;
			
			s += "\t["+ (qChar) +"] "+ question.getAnswers(i) + "%n";
		}
		return s;
	}
}
