package game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
	public Socket sock;
	public String name;
	public PrintWriter output;
	public BufferedReader input;
	public boolean DQd;
	public boolean busy;
	public int moveLimit;
	public double gameTimeRemaining, gameTimeLimit;
	public int finalPlayLimit;
	public int maxWarnings;
	public boolean deterministic;
	public static final boolean DUMP = false;
	public Client(ServerSocket mainSocket, int initTimeLimit,
					int pLimit, int fLimit,
					int gLimit, int nWarnings) throws Exception
	{
		sock = mainSocket.accept();
		output = new PrintWriter(sock.getOutputStream(), true);
		input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		name = hardLimitResponse(initTimeLimit);
		if (name == null) {
			DQd = true;
			name = "DQd on initialization";
			return;
		}
		String playerType = hardLimitResponse(10);
		if (playerType == null) {
			DQd = true;
			name += "DQd init";
			playerType = "DETERMINISTIC";
			return;
		}
		deterministic = (playerType.equals("DETERMINISTIC"));
		maxWarnings = nWarnings;
		moveLimit = pLimit;
		finalPlayLimit = fLimit;
		gameTimeLimit = gLimit;
		busy = false;
	}
	public void simpleMsg(String s)
	{
		if (DUMP) System.err.println("SDUMP1/1: " + s);
		output.println(s);
		if (DUMP) System.err.println("EDUMP");
	}
	public void simpleMsg(String s1, String s2)
	{
		if (DUMP) {
			System.err.println("DUMP1/2: " + s1);
			System.err.println("DUMP2/2: " + s2);
		}
		output.println(s1);
		output.println(s2);
		if (DUMP) System.err.println("EDUMP");
	}
	public void simpleMsg(String s1, String s2, String s3)
	{
		if (DUMP) {
			System.err.println("DUMP1/3: " + s1);
			System.err.println("DUMP2/3: " + s2);
			System.err.println("DUMP3/3: " + s3);
		}
		output.println(s1);
		output.println(s2);
		output.println(s3);
		if (DUMP) System.err.println("EDUMP");
	}
	public String hardLimitResponse(int seconds)
	{
		String name = null;
		try {
			sock.setSoTimeout(seconds * 1000);
			name = input.readLine();
		}
		catch (Exception e) {
			System.err.printf("init timeout %s %d%n", e.toString(), seconds);
			System.err.flush();
		}
		return name;
	}
	public double timedResponse(double seconds, GameMove move)
	{
		long start = System.currentTimeMillis();
		try {
			sock.setSoTimeout((int)(seconds * 1000));
			if (DUMP) {
				System.err.println("Timed response: " + seconds);
			}
			String mvStr = input.readLine();
			if (DUMP) {
				System.err.println("RESPONSE: " + mvStr);
			}
			long diff = System.currentTimeMillis() - start;
			
			output.println("TIME");
			output.println(diff/1000.0);
			move.parseMove(mvStr);

			double elapsedTime = diff / 1000.0;
			return elapsedTime;
		}
		catch (Exception e) {
			System.err.printf("timeout %s%n", name);
			output.println("TIME");
			output.println(seconds + 10.0);
			return -1; 
		}
	}
}
