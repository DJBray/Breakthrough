package game;
import java.io.*;
import java.net.*;

public abstract class GamePlayer {
	protected GameState gameState;
	protected GameState.Who side;
	protected boolean deterministic;
	protected String nickname;
    
	public Params tournamentParams = new Params("config" + File.separatorChar + "tournament.txt");
	
	/**
	 * Produces the player's move, given the current state of the game.
	 * This function must return a value within the time alloted by the
	 * game timing parameters.
	 * @param state Current state of the game
	 * @param lastMv Opponent's last move. "--" if it is game's first move. 
	 * @return Player's move
	 */
	public abstract GameMove getMove(GameState state, String lastMv); // do your thinking here
	/**
	 * Initializes the player at the beginning of the tournament. This is called
	 * once, before any games are played. This function must return within
	 * the time alloted by the game timing parameters. Default behavior is
	 * to do nothing.
	 */
	public void init()
	{ }
	/**
	 * This is called to register the opponent's message to this player.
	 * This is called after getMessageForOpponent
	 * @param msg Message sent by opposing player
	 */
	public void messageFromOpponent(String msg)
	{ }
	/**
	 * This is called to obtain the string this player wants to send
	 * to its opponent.	This is called before getMessageFromOpponent
	 * @return The string to be sent to the opponent
	 * @param opponent Name of the opponent being played
	 */
	public String messageForOpponent(String opponent)
	{ return ""; }
	/**
	 * This is called at the start of a new game. This should be relatively
	 * fast, as the player must be ready to respond to a move request, which
	 * should come shortly thereafter. The side being played (HOME or AWAY)
	 * is stored in the side data member. Default behavior is to do nothing. 
	 * @param opponent Name of the opponent being played
	 */
	public void startGame(String opponent)
	{ }
	/**
	 * Called to inform the player how long the last move took. This can
	 * be used to calibrate the player's search depth. Default behavior is
	 * to do nothing.
	 * @param secs Time for the server to receive the last move
	 */
	public void timeOfLastMove(double secs)
	{ }
	/**
	 * Called when the game has ended. Default behavior is to do nothing. 
	 * @param result -1 if loss, 0 if draw, +1 if 
	 */
	public void endGame(int result)
	{ }
	/**
	 * Called at the end of the tournament. Can be used to do
	 * housekeeping tasks. Default behavior is to do nothing.
	 */
	public void done()
	{ }
	/**
	 * Constructs a game player
	 * @param nickname Mascot name of team
	 * @param isDeterministic true if player is completely deterministic
	 */
	public GamePlayer(String nickname, GameState gs, boolean isDeterministic)
	{
		nickname = nickname.replace(':', ';');
		nickname = nickname.replace('/', ';');
		nickname = nickname.replace('\\', ';');
		nickname = nickname.replace('*', ';');
		nickname = nickname.replace('?', ';');
		nickname = nickname.replace('"', ';');
		nickname = nickname.replace('<', ';');
		nickname = nickname.replace('>', ';');
		nickname = nickname.replace('|', ';');
		this.nickname = nickname;
		this.gameState = gs;
		this.deterministic = isDeterministic;
	}

	private void compete(BufferedReader input, PrintWriter output, int dumpLevel)
	{
		try {
			init();
			output.println(nickname);
			if (deterministic) {
				output.println("DETERMINISTIC");
			} else {
				output.println("STOCHASTIC");
			}

			while (true) {
				String cmd = input.readLine();
				if (cmd.equals("DONE")) {
					if (dumpLevel > 0)
						System.out.println(nickname + "is done playing");
					done();
					break;
				} else if (cmd.equals("START")) {
					side = GameState.str2who(input.readLine());
					String opp = input.readLine();
					if (dumpLevel > 0)
						System.out.printf("\"%s\" new game as %s against \"%s\"%n", nickname, side, opp);
					String msg = messageForOpponent(opp);
					if (dumpLevel > 0)
						System.out.println("Message for opponent: " + msg);
					output.println(msg);
					msg = input.readLine();
					messageFromOpponent(msg);
					if (dumpLevel > 0)
						System.out.println("Message from opponent: " + msg);
					startGame(opp);
				} else if (cmd.equals("OVER")) {
					String winner = input.readLine();
					output.println("OVER");
					if (winner.equals("DRAW")) {
						if (dumpLevel > 0)
							System.out.println("I (" + nickname + ") had a draw");
						endGame(0);
					} else if (GameState.str2who(winner) == side) {
						if (dumpLevel > 0)
							System.out.println("I (" + nickname + ") won");
						endGame(+1);
					} else {
						if (dumpLevel > 0)
							System.out.println("I (" + nickname + ") lost");
						endGame(-1);
					}
				} else if (cmd.equals("MOVE")) {
					String lastMove = input.readLine();
					String boardStr = input.readLine();
					gameState.parseMsgString(boardStr);

					if (dumpLevel > 1) {
						System.out.printf("Turn %s (%s)%n", nickname, side);
						System.out.printf("Last move: %s%n", lastMove.toString());
						System.out.println("Current state\n" + gameState);
					}
					GameMove mv = getMove(gameState, lastMove);
					if (dumpLevel > 1)
						System.out.println("Sending my move: " + mv);
					output.println(mv.toString());
					String timeStr = input.readLine();	// should be "TIME"
					if (!timeStr.equals("TIME")) {
						System.err.println("time message" + timeStr);
					}
					double time = Double.parseDouble(input.readLine());
					if (dumpLevel > 1)
						System.out.printf("%f secs%n", time);
					timeOfLastMove(time);
				} else {
					System.err.println("bad command from server: " + cmd);
				}
			}
		}
		catch (Exception e) {
			System.err.println("Problem in " + nickname + " " + e);
			e.printStackTrace();
			System.err.flush();
		}
	}
	/**
	 * Used to compete the player in a tournament.
	 * @param args command line arguments passed to the GamePlayer
	 */
	public void compete(String [] args)
	{ compete(args, 2); }
	/**
	 * Used to compete the player in a tournament.
	 * @param args command line arguments passed to the GamePlayer
	 * @param dumpLevel 0, 1, 2 indicating how much info to display to console
	 */
	public void compete(String [] args, int dumpLevel)
	{
		String host = tournamentParams.string("HOST");
		
		int port = args.length == 0 ? tournamentParams.integer("PORT") : Integer.parseInt(args[1]);
		
		try {
			Socket socket = new Socket(host, port);
			PrintWriter output  = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Connected to server, now waiting to play");
			compete(input, output, dumpLevel);
		}
		catch (Exception e) {
			System.err.println("Error connecting to" + host + " " + port);
		}
		
		System.out.println(nickname + " tournament over");
	}
}
