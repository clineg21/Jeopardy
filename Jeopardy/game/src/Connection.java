import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection
{
	private Socket sock;
	private PrintWriter pw;
	private BufferedReader br;
	private boolean debug;
	
	/*
	 * a constructor for Connection
	 * takes a Socket as a parameter
	 */
	public Connection(Socket s)
	{
		// get the socket
		sock = s;
		// create readers and writers
		try {
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			pw = new PrintWriter(sock.getOutputStream(), true);
		}
		catch (Exception e)
		{ 
			//System.out.println("ERROR: <" + e + "> making connection");
		}
		
		// default for debugging is false
		debug = false;
	}
	
	/*
	 * set debug to true to display input to screen
	 */
	public void setDebug(boolean d)
	{
		debug = d;
	}
	
	/*
	 * a method to read a string
	 */
	public String readString()
	{
		String s = "";
		// read a string from the buffer
		try {
			s = br.readLine();
		}
		catch (Exception e)
		{
			//System.out.println("ERROR: <" + e + "> reading string");
		}
		
		// if debugging- display the string that was read
		if(debug == true)
		{
			System.out.println(s);
		}
		
		// return read string
		return s;
	}
	
	/*
	 * a method to send a string
	 */
	public void sendString(String s)
	{
		// if debugging print the string being sent
		if(debug == true)
		{
			System.out.println(s);
		}
		// send the string to the PrintWriter
		pw.println(s);
	}
	
	/*
	 * a method to read a string and convert to an int
	 */
	public int readInt()
	{
		return Integer.valueOf(readString());
	}
	
	/*
	 * a method to read a string and convert to a char
	 */
	public char readChar() {
		String s = readString().trim();
		// if only a single char was entered
		if(s.length() == 1)
			return s.charAt(0);
		// if multiple chars were entered
		return 'z';
	}
}