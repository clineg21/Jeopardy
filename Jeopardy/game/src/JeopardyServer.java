import java.io.File;
import java.net.Socket;
import java.util.Random;
import java.net.*;
import java.io.IOException;
import java.util.Scanner; 

// Jeopardy class
public class JeopardyServer {
	private Repository repo;
	private Board board;
	private Player[] players;
	private ServerSocket server; 

 
	/*
	 * a Jeopardy constructor
	 */
	private JeopardyServer(int port) throws IOException {
		// misc game setup
		repo = new Repository("CategoriesRepo/Categories.txt");
		board = new Board();
		
		// server setup
		server = new ServerSocket(port);
		System.out.println("Starting server");
	}
	
	/*
	 *  a method to setup the game
	 */
	private void setup() throws Exception {
		// get board dimensions from setup file
		// get a scanner to read the file
		File file = new File("Resources/Setup.txt");
		Scanner s = new Scanner(file);

		// get first line of file- the number of categories
		String line = s.nextLine();
		String[] data = line.split("//");
		int totalCats = Integer.parseInt(data[0].trim());
		// get 2nd line of file- the number of questions
		line = s.nextLine();
		data = line.split("//");
		int totalQues = Integer.parseInt(data[0].trim());
		// get 3rd line of file- the number of daily doubles
		line = s.nextLine();
		data = line.split("//");
		int totalDD = Integer.parseInt(data[0].trim());
		
		// create the board
		boolean b = board.CreateBoard(repo, totalCats, totalQues, totalDD);
		
		// validate board creation
		if(!b)
			System.out.println("ERROR: The board could not be created");
		else {
			// populate the board
			board.PopulateBoard(repo);
		}

		
		// get the 4th line of the file- number of players
		line = s.nextLine();
		data = line.split("//");
		int numPlayers = Integer.parseInt(data[0].trim());
		
		// make the array to hold the players
		players = new Player[numPlayers];
		
		// get each player's name
		for(int i = 0; i < players.length ; i++) {
			// make connection
			Socket soc = server.accept();
			Connection conn = new Connection(soc);
			System.out.println((i+1) + " connected.\n");
			
			// read names from connections
			String n = conn.readString().trim();
			// validate names
			while (n.equals(null) || n.equals("")) {
				System.out.println("Invalid name");
				conn.sendString(String.format("%d", Signals.ERR.ordinal()));
				n = conn.readString().trim();
			}
			conn.sendString(String.format("%d", Signals.ACK.ordinal()));
			// create player object & add to list
			players[i] = new Player(n, conn);
			System.out.println("Player " + (i+1) + ": " + players[i].getName() + "\n");
		}
		
		// close file scanner
		s.close();	
	}
	
	/*
	 * a method to return the final score for network game
	 */
	private String FinalScoresNetwork(int i) {
		int score = players[i].GetScore();
		String name = players[i].getName();
		// if player has positive points
		if(score > 0) {
			return "Congrats, " + name + "! You won " + score + "!!!";
		}
		// if player has negative/zero points
		else {
			return "Sorry, " + name + ", your score was " + score + " so you didn't win anything today :(";
		}
	}
	
	/*
	 * delay method for 5000 seconds
	 */
	private void delay() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}

	/*
	 * a run method for the Jeopardy game
	 */
	private void run() throws Exception {
		
		// do setup
		try {
			setup();
		}
		catch (Exception e) {
			System.out.println("Error: <" + e + "> setup could not be completed");
			// end the run of the game if problem
			return;
		}
		
		// get a first player
		int p;
		// for single player p = 0
		if(players.length == 1) {
			p = 0;
		}
		// for multiplayer p = random
		else {
			Random r = new Random();
			p = r.nextInt(players.length);
		}
		
		do {
			// get current player's connection
			Connection pconn = players[p].getConn();
			
			// display all player's score
			for(int i = 0; i < players.length; i++) {
				Connection conn = players[i].getConn();
				System.out.println("Sending score");
				conn.sendString(players[i].getName() + "'s score: " + players[i].GetScore());
			}
			
			// display the board
			System.out.println("Sending board");
			pconn.sendString(board.displayToStr(repo));
			
			// setup variables w. default values
			int c = 0; 			// to hold the cateogry
			Integer qu = 0;		// to hold the question
			BoardQuestion q;	// to hold the boardquestion
			Character guess;	// to hold the guess

			// choose a category
			c = pconn.readInt();
			System.out.println();
			// validate
			while(!board.CatIsValid(c)) {
				System.out.println("Invalid category");
				pconn.sendString(String.format("%d", Signals.ERR.ordinal()));
				c = pconn.readInt();
			}
			pconn.sendString(String.format("%d", Signals.ACK.ordinal()));
			System.out.println("Category [" + c + "] chosen.");
			
			// choose a question
			qu = pconn.readInt();
			System.out.println();
			// validatae
			while(!board.QuestIsValid(qu)) {
				// ask again
				System.out.println("Invalid question");
				pconn.sendString(String.format("%d", Signals.ERR.ordinal()));
				qu = pconn.readInt();
			}
			pconn.sendString(String.format("%d", Signals.ACK.ordinal()));
			System.out.println("Category [" + c + "] for " + qu + " chosen.");
			
			// retrieve question from the board
			// if the question has been answered break loop and start over
			q = board.RetrieveContent(c, qu);
			if(q == null) {
				//System.out.println("Oops, that question was already answered.\n");
				System.out.println("Question already answered");
				pconn.sendString(String.format("%d", Signals.ERR.ordinal()));
				pconn.sendString(String.format("%d", Signals.GAMECONT.ordinal()));
				continue;
			}
			pconn.sendString(String.format("%d", Signals.ACK.ordinal()));
			
			
			// send if daily double
			if(q.getDailyDouble() == true) {
				System.out.println("\nDaily double"); 
				// if the player can wager
				if(players[p].GetScore() > 0) {
					pconn.sendString(String.format("%d", Signals.DDW.ordinal()));
					pconn.sendString("%nCongrats! You selected a daily double!%nYou may choose to wage any amount between 0 and " + players[p].GetScore() + " but it must be a multiple of 100"
							+ "%nHow much would you like to wager?: ");
					// get wager
					qu = pconn.readInt();
					// must be multiple of 100 & less that score
					while(qu % 100 != 0 && players[p].wageIsValid(qu) || !(players[p].wageIsValid(qu))) {
						pconn.sendString(String.format("%d", Signals.ERR.ordinal()));
						qu = pconn.readInt();
					}
					pconn.sendString(String.format("%d", Signals.ACK.ordinal()));
				}
				// if they can't wager
				else {
					pconn.sendString(String.format("%d", Signals.DDNW.ordinal()));
				}
			}
			// if it's not a daily double
			else {
				pconn.sendString(String.format("%d", Signals.NDD.ordinal()));
			}

			// sends question and answer
			pconn.sendString(q.bqToString());
			delay();
			// get the player's guess
			guess = pconn.readChar();
			// make the guess: correct or not
			qu = board.MakeGuess(guess, q, qu);
			
			// if guess timed out
			if(guess == 't') {
				System.out.println("\nRan out of time\n");
				pconn.sendString("%n%nWhoops you ran out of time, better luck next time%nThe correct answer was: " + q.getStringAnswer() + "%nSubtracting " + (qu * -1) + " points from " + players[p].getName() +"%n");
				// subtract points
				players[p].SetScore(qu);
			}
			// if answer was invalid
			else if(qu == null){
				//not valid 
				System.out.println("\nInvalid answer\n");
				pconn.sendString("%nSorry there was a problem with your answer. %nThe correct answer was: " + q.getStringAnswer() +"%nNo points added or subtracted%n");
			}
			// if answer was correct
			else if(qu > 0) {
				//correct 
				System.out.println("\nCorrect answer\n");
				pconn.sendString("%nCongrats! You got the correct answer!%nAdding " + qu + " points to " + players[p].getName() + "%n");
				// add points
				players[p].SetScore(qu);
			}
			// answer was incorrect
			else {
				//incorrect
				System.out.println("\nIncorrect answer\n");
				pconn.sendString("%n%nSorry that was the wrong answer. %nThe correct answer was: " + q.getStringAnswer() + "%nSubtracting " + (qu * -1) + " points from " + players[p].getName() + "%n");
				// subtract points
				players[p].SetScore(qu);
			}
			
			// are there questions remaining? send to server
			if(board.QuestionsRemaining() > 0) {
				pconn.sendString(String.format("%d", Signals.GAMECONT.ordinal()));
			}
			else {
				pconn.sendString(String.format("%d", Signals.GAMEOVER.ordinal()));
			}
						
		} while(board.QuestionsRemaining() > 0);
		
		// get final score message
		try {	
			for(int i = 0; i < players.length; i++) {
				String f = FinalScoresNetwork(i);
				System.out.println("Final score: " + players[i].GetScore());
				players[i].getConn().sendString(f);
			}
		}
		catch(NumberFormatException e) {
			System.out.println("\n Unable to print player score");
			
		}
		
		System.out.println("\nShutting down server");
		server.close();
	}
	/*
	 * a main method for Jeopardy
	 */
	public static void main(String[] args) throws Exception {
		JeopardyServer jeopardy = new JeopardyServer(8081);
		// run the game
		jeopardy.run();
	}

}