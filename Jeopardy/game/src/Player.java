public class Player {
	private String name;
	private int score;
	InputScanner scan;
	Connection conn;
	
	/*
	 * a constructor
	 */
	Player(String name, Connection conn){
		this.name = name;
		score = 0;
		scan  = new InputScanner();
		this.conn = conn;
	}
	
	/*
	 * get the player's name
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * get the player's connection w. a client
	 */
	public Connection getConn() {
		return conn;
	}

	/*
	 * get the player's score
	 */
	public int GetScore() {
		return score;
		
	}
	
	/*
	 * add to the score
	 */
	public void SetScore(int score) {
		this.score += score;
	}
	
	/*
	 * is a wager valid for this player to make
	 */
	public boolean wageIsValid(Integer wage) {
		if(wage < 0 || wage > score )
			return false;
		return true;
	}
}
