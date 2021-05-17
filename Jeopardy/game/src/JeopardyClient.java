import java.io.IOException;
import java.net.Socket;

public class JeopardyClient {
	private Connection conn;
	private String name;
	private Socket socket;
	private InputScanner scan;
	
	/*
	 * a constructor
	 */
	public JeopardyClient() throws IOException {
		scan  = new InputScanner();
	}

	/*
	 * a run method for the game- client side
	 */
	public void run() throws Exception {
		// print instructions
		Instructions();
		
		// get player's name
		System.out.print("Please enter your name: ");
		name = scan.readLine();
		
		// make connection
		socket = new Socket("192.155.95.81", 8081);
		conn = new Connection(socket);
		
		// send the name
		conn.sendString(name);
		// read validation
		int val = conn.readInt();
		// validate name
		while(val == Signals.ERR.ordinal()) {
			// get a new name from user
			System.out.print("Please enter a valid name: ");
			name = scan.readLine();
			conn.sendString(name);
			// continue validation
			val = conn.readInt();
		}
		
		// the game control for loop
		do {
			// get player's score
			System.out.println("\n" + conn.readString() + "\n");
			
			// get the board- use printf
			System.out.printf(conn.readString());
			
			// choose a category
			System.out.print("Please enter the category you would like (1, 2, etc): ");
			int c = Choose();
			conn.sendString(String.format("%d", c));
			if(c == -2) {
				System.out.printf(conn.readString());
			}
			// validation
			val = conn.readInt();
			while(val == Signals.ERR.ordinal()) {
				// get a new name
				System.out.print("\nPlease enter a valid category (1, 2, etc): ");
				c = Choose();
				conn.sendString(String.format("%d", c));
				if(c == -2) {
					System.out.printf(conn.readString());
				}
				// get validation
				val = conn.readInt();
			}
			
			// choose a category
			System.out.print("\nPlease enter the question you would like (100, 200, etc): ");
			int qu = Choose();
			conn.sendString(String.format("%d", qu));
			if(qu == -2) {
				System.out.printf(conn.readString());
			}
			// validation
			val = conn.readInt();
			while(val == Signals.ERR.ordinal()) {
				// get a new name
				System.out.print("\nPlease enter a valid question (100, 200, etc): ");
				qu = Choose();
				conn.sendString(String.format("%d", qu));
				if(qu == -2) {
					System.out.printf(conn.readString());
				}
				// get validation
				val = conn.readInt();
			}
			
			// get if question is already answered
			val = conn.readInt();
			if(val != Signals.ERR.ordinal()) {
				// is the question a daily double
				val = conn.readInt();
				// if they need to wager
				if(val == Signals.DDW.ordinal()) {
					// read the message and display
					System.out.printf(conn.readString());
					// get the wager
					qu = Choose();
					conn.sendString(String.format("%d", qu));
					// validate
					val = conn.readInt();
					while (val == Signals.ERR.ordinal()) {
						// get a valid wager
						System.out.print("\nPlease enter a valid wager: ");
						qu = Choose();
						conn.sendString(String.format("%d", qu));
						val = conn.readInt();
					}
					// show the wager
					System.out.println("\nYou have wagered " + qu);
				}
				// not enough money to wager
				else if(val == Signals.DDNW.ordinal()) {
					System.out.println("\nThis is a daily double, but unfortunately" 
					+" you don't have enough money to wager");
					System.out.println("You can still answer it as a normal question");
				}
				
				//get question & answers 
				System.out.println("\nYou will have 10 seconds to answer the question.");
				System.out.printf(conn.readString());
				
				// get an answer from user
				System.out.print("You have 10 seconds to enter your answer (a, b, c, d): ");
				conn.sendString(String.format("%c",ChooseAns()));
				
				// display results
				System.out.printf(conn.readString());
			}
			// if the question was already answered
			else {
				System.out.println("\nOops, that question was already answered.\n");
			}
			// read if game continues
			val = conn.readInt();
			
		} while(val == Signals.GAMECONT.ordinal());
		
		// final score
		System.out.println("\n\n--------Final Scores--------\n");
		String f = conn.readString();
		System.out.println(f);
		System.out.println("\n----------------------------");
	}
	
	/*
	 * read an int (with error handing) not timed
	 */
	private int Choose() {
		int i;
		try {
			// read an int
			i = scan.readInt();
		}
		catch(Exception e) {
			//System.out.println("ERROR: <" + e + "> could not read category from scanner");
			i = -1;
		}
		return i;
	}

	/*
	 * choose a char answer (with error handling)
	 */
	private Character ChooseAns() {
		Character g;
		try {
			// read a char
			g = scan.timeReadChar(10000, 100, 't');
		}
		catch(Exception e) {
			//System.out.println("ERROR: <" + e + "> could not read answer from scanner");
			g = null;
		}
		return g;
	}
	
	/*
	 * instructions for playing the game
	 */
	private void Instructions()
	{
		System.out.println("Welcome to Jeopardy! \n\n***Single Player Mode***\n"
				+ "\tTo play, pick a category and a point value. \n"
				+ "\tThe question will appear & you will have 7.5 seconds to answer \n"
				+ "\tOnce answered…\n"
				+ "\t\tIf the player is correct, the point value of the question is added to score \n"
				+ "\t\tIf the player is incorrect, the point value of the question is\n"
				+ "\t\t\tdeducted from the score and the correct answer will appear \n"
				+ "\tThe answered question will then appear as an X on the board \n"
				+ "\tThe game will continue until all questions have been answered \n\n"
				+ "***Daily Double Rules***\n"  
				+ "\tThe player can wager up to their score\n"
				+ "\tThe player’s score must be greater than zero to be eligible to wager\n");
	}
	
	/*
	 * a main method
	 */
	public static void main(String[] args) throws Exception {
		JeopardyClient jc = new JeopardyClient();
		// run the client
		jc.run();
	}

}
