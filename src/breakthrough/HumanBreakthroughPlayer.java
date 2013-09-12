package breakthrough;
import game.*;

public class HumanBreakthroughPlayer extends RandomBreakthroughPlayer {
	private GameFrame frame;
	private BreakthroughMove move = new BreakthroughMove(0,0,0,0);

	public HumanBreakthroughPlayer(String nname)
	{
		super(nname);
		frame = new GameFrame(nickname, new BreakthroughCanvas());
		frame.setVisible(true);
		gameState = new BreakthroughState();
	}
	public GameMove getMove(GameState game, String lastMove)
	{
		char ch = side == GameState.Who.HOME ? BreakthroughState.homeSym : BreakthroughState.awaySym;
		frame.setTitle("My move (" + ch + ")");
		if (!lastMove.equals("--") && frame.canvas.move != null) {
			((BreakthroughMove)frame.canvas.move).parseMove(lastMove);
		}
		frame.canvas.setBoard(game);
		frame.canvas.repaint();
		frame.canvas.getMove(move, game, this);
		try {
			frame.canvas.ready.acquire();
		}
		catch (Exception e) { 		}
		game.makeMove(move);
		frame.canvas.repaint();
		frame.setTitle("Waiting");
		return move;
	}
	public void endGame(int result)
	{
		char ch = side == GameState.Who.HOME ? BreakthroughState.homeSym : BreakthroughState.awaySym;
		if (result == 1) {
			frame.setTitle("Won (" + ch + ")");
		} else if (result == -1) {
			frame.setTitle("Loss (" + ch + ")");
		} else {
			frame.setTitle("Draw (" + ch + ")");
		}
	}
	public static void main(String[] args)
	{
		GamePlayer p = new HumanBreakthroughPlayer("HUMAN");	// must have this name
		p.compete(args);
	}

}
