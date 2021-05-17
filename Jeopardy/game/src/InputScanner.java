import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/*
 * InputScanner class to read input from the console
 */
public class InputScanner {

	private BufferedReader br;
	
	/*
	 * a constructor to make a buffered reader
	 */
	InputScanner(){
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/*
	 * a method to read an int from the console
	 */
	public int readInt() throws Exception {
		return Integer.parseInt(readLine());
	}
	
	/*
	 * a method to read a string from the console
	 */
	public String readLine() throws Exception {
		return br.readLine();
	}
	
	/*
	 * a method to read a single character from the console
	 */
	public char readChar() throws Exception {
		return (char)br.read();
	}
	
	/*
	 * reads a string after a specified delay
	 */
	public String nextStringTimed(long delay, long slice, String def) throws Exception{
		String s = def;
		long elapsed = 0;
		
		// delay for specified number of milliseconds
		while(elapsed < delay && br.ready() != true) {
			TimeUnit.MILLISECONDS.sleep(slice);
			elapsed += slice;
		}
		
		// after the delay- read a string
		if(elapsed < delay) {
			s = readLine();
		}
		
		return s;
	}
	
	/*
	 * reads an int after a delay
	 */
	public int timeReadInt(long delay, long slice, int def) throws Exception {
		return Integer.valueOf(nextStringTimed(delay, slice, String.valueOf(def)));	
	}
	
	/*
	 * reads a char after a delay
	 */
	public Character timeReadChar(long delay, long slice, char def) throws Exception {
		String s = nextStringTimed(delay, slice, Character.toString(def));
		s.trim();
		// if they entered a single char
		if(s.length() == 1)
			return s.charAt(0);
		// otherwise return something that won't be an answer choice
		return 'z';
	}
}
